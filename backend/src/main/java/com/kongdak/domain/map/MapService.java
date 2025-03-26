package com.kongdak.domain.map;

import com.kongdak.domain.map.dto.request.CreateReviewRequest;
import com.kongdak.domain.map.dto.request.UpdateReviewRequest;
import com.kongdak.domain.map.dto.response.*;
import com.kongdak.domain.member.entity.Member;
import com.kongdak.domain.member.repository.MemberRepository;
import com.kongdak.global.exception.BusinessException;
import com.kongdak.global.exception.ErrorCode;
import com.kongdak.global.redis.RedisPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MapService {

    @Value("${kakao.api.rest-api-key}")
    private String restApiKey;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://dapi.kakao.com")
            .build();

    private final MapRepository mapRepository;
    private final MemberRepository memberRepository;
    private final PlaceImageRepository imageRepository;
    private final PlaceReviewRepository reviewRepository;
    private final PlaceOperatingHourRepository operatingHourRepository;
    private final RedisPlaceRepository redisPlaceRepository;

    private static final String KEY_PREFIX = "place:";
    private final StringBuilder redisKeyBuilder = new StringBuilder(KEY_PREFIX);

    // 카카오 Search API 사용
    @Transactional
    public KakaoLocalSearchResponse search(String query, String x, String y, String size, String sort) {
        KakaoLocalSearchResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/local/search/keyword.json")
                        .queryParam("query", query)
                        .queryParamIfPresent("x", Optional.ofNullable(x))
                        .queryParamIfPresent("y", Optional.ofNullable(y))
                        .queryParamIfPresent("size", Optional.ofNullable(size))
                        .queryParamIfPresent("sort", Optional.ofNullable(sort))
                        .build())
                .header("Authorization", "KakaoAK " + restApiKey)
                .retrieve()
                .bodyToMono(KakaoLocalSearchResponse.class)
                .block();

        List<Place> placesToSave = response.documents().stream()
                .filter(document -> {
                    redisKeyBuilder.append(document.id());

                    boolean notExists = !redisPlaceRepository.exists(redisKeyBuilder.toString());

                    if (notExists) {
                        redisPlaceRepository.savePlaceId(redisKeyBuilder.toString());
                    }

                    redisKeyBuilder.setLength(KEY_PREFIX.length());
                    return notExists;
                })
                .map(document -> Place.builder()
                        .placeId(Long.parseLong(document.id()))
                        .placeName(document.place_name())
                        .categoryName(document.category_name())
                        .addressName(document.address_name())
                        .roadAddressName(document.road_address_name())
                        .phone(document.phone())
                        .longitude(document.x())
                        .latitude(document.y())
                        .build())
                .toList();

        mapRepository.saveAll(placesToSave);

        return response;

    }

    // 장소 상세 정보 조회
    public PlaceDetailResponse getPlaceDetail(Long placeId) {

        Place place = mapRepository.findByPlaceId(placeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PLACE_NOT_FOUND));

        List<String> previewImages = imageRepository.findTop3ByPlaceId(placeId, PageRequest.of(0, 3)).stream()
                .map(PlaceImage::getImageUrl)
                .toList();

        List<PlaceReviewResponse> previewReviews = reviewRepository.findTop3ByPlaceId(placeId, PageRequest.of(0, 3)).stream()
                .map(PlaceReviewResponse::from)
                .toList();

        List<PlaceOperatingHourResponse> operatingHours = operatingHourRepository.findByPlaceId(placeId).stream()
                .map(PlaceOperatingHourResponse::from)
                .toList();

        return PlaceDetailResponse.of(
                place.getPlaceId(),
                place.getPlaceName(),
                place.getCategoryName(),
                place.getAddressName(),
                place.getRoadAddressName(),
                place.getPhone(),
                previewImages,
                previewReviews,
                operatingHours
        );

    }

    // 장소 리뷰 조회
    public PagedReviewResponse getReviewsByPlace(Long placeId, int page) {
        int pageSize = 5;
        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by("createdAt").descending());

        Page<PlaceReviewResponse> reviewPage = reviewRepository.findByPlace_PlaceId(placeId, pageRequest)
                .map(PlaceReviewResponse::from);
        return PagedReviewResponse.from(reviewPage);
    }

    // 사용자 리뷰 조회
    public PagedReviewResponse getMyReviews(Long memberId, int page) {
        int pageSize = 5;
        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by("createdAt").descending());

        Page<PlaceReviewResponse> reviewPage = reviewRepository
                .findByMember_Id(memberId, pageRequest)
                .map(PlaceReviewResponse::from);

        return PagedReviewResponse.from(reviewPage);
    }

    // 장소 리뷰 상세 조회
    public PlaceReviewResponse getReviewById(Long reviewId) {
        PlaceReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND));

        return PlaceReviewResponse.from(review);
    }

    // 장소 리뷰 작성
    @Transactional
    public PlaceReviewResponse createReview(Long placeId, Long memberId, CreateReviewRequest request) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        Place place = mapRepository.findByPlaceId(placeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PLACE_NOT_FOUND));

        PlaceReview review = PlaceReview.builder()
                .place(place)
                .member(member)
                .rating(request.rating())
                .comment(request.comment())
                .build();

        reviewRepository.save(review);

        Optional.ofNullable(request.imageUrls())
                .filter(urls -> !urls.isEmpty())
                .ifPresent(urls -> urls.forEach(url -> {
                    PlaceImage image = PlaceImage.builder()
                            .imageUrl(url)
                            .build();
                    review.addImage(image);
                }));

        return PlaceReviewResponse.from(review);
    }

    // 장소 리뷰 수정
    @Transactional
    public PlaceReviewResponse updateReview(Long reviewId, Long memberId, UpdateReviewRequest request) {

        PlaceReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND));

        if(!review.getMember().getId().equals(memberId)) {
            throw new BusinessException(ErrorCode.NOT_YOUR_REVIEW);
        }

        review.updateReview(request.rating(), request.comment());

        Optional.ofNullable(request.deleteImageIds())
                .filter(ids -> !ids.isEmpty())
                .ifPresent(ids -> ids.forEach(imageId -> {
                    PlaceImage image = imageRepository.findById(imageId)
                            .orElseThrow(() -> new BusinessException(ErrorCode.PLACE_IMAGE_NOT_FOUND));
                    review.removeImage(image);
                    imageRepository.delete(image);
                }));

        Optional.ofNullable(request.newImageUrls())
                .filter(urls -> !urls.isEmpty())
                .ifPresent(urls -> urls.forEach(url -> {
                    PlaceImage image = PlaceImage.builder()
                            .imageUrl(url)
                            .build();
                    review.addImage(image);
                }));

        return PlaceReviewResponse.from(review);

    }

    // 장소 리뷰 삭제
    @Transactional
    public PlaceReviewDeleteResponse deleteReview(Long reviewId, Long memberId) {

        PlaceReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND));

        if (!review.getMember().getId().equals(memberId)) {
            throw new BusinessException(ErrorCode.NOT_YOUR_REPLY);
        }

        reviewRepository.delete(review);

        return PlaceReviewDeleteResponse.of(review);

    }


}
