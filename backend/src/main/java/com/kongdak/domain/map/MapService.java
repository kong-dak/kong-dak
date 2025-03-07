package com.kongdak.domain.map;

import com.kongdak.controller.dto.response.KakaoLocalSearchResponse;
import com.kongdak.controller.dto.response.PlaceDetailResponse;
import com.kongdak.controller.dto.response.PlaceOperatingHourResponse;
import com.kongdak.controller.dto.response.PlaceReviewResponse;
import com.kongdak.global.exception.BusinessException;
import com.kongdak.global.exception.ErrorCode;
import com.kongdak.global.redis.RedisPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final PlaceImageRepository imageRepository;
    private final PlaceReviewRepository reviewRepository;
    private final PlaceOperatingHourRepository operatingHourRepository;
    private final RedisPlaceRepository redisPlaceRepository;

    private static final String KEY_PREFIX = "place:";
    private final StringBuilder redisKeyBuilder = new StringBuilder(KEY_PREFIX);

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

    public PlaceDetailResponse getPlaceDetail(Long placeId) {

        Place place = mapRepository.findByPlaceId(placeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PLACE_NOT_FOUND));

        List<String> previewImages = imageRepository.findTop3ByPlaceId(placeId).stream()
                .map(PlaceImage::getImageUrl)
                .toList();

        List<PlaceReviewResponse> previewReviews = reviewRepository.findTop3ByPlaceId(placeId).stream()
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

}
