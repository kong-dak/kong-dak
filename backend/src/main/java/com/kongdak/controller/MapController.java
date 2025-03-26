package com.kongdak.controller;

import com.kongdak.domain.map.MapService;
import com.kongdak.domain.map.dto.request.CreateReviewRequest;
import com.kongdak.domain.map.dto.request.UpdateReviewRequest;
import com.kongdak.domain.map.dto.response.*;
import com.kongdak.global.exception.ErrorResponse;
import com.kongdak.global.response.BaseResponse;
import com.kongdak.global.security.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/maps")
@Tag(name = "지도", description = "지도 관련 API")
public class MapController {

    private final MapService mapService;

    @Operation(
            summary = "키워드로 장소 검색",
            description = "카카오 로컬 API를 활용하여 키워드로 장소를 검색합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "검색 성공",
                    content = @Content(schema = @Schema(implementation = KakaoLocalSearchResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping("/search")
    public BaseResponse<KakaoLocalSearchResponse> search(
            @Parameter(description = "검색 키워드", required = true) @RequestParam String query,
            @Parameter(description = "중심 좌표 X (경도)") @RequestParam(required = false) String x,
            @Parameter(description = "중심 좌표 Y (위도)") @RequestParam(required = false) String y,
            @Parameter(description = "결과 개수") @RequestParam(defaultValue = "15") String size,
            @Parameter(description = "정렬 기준 (accuracy/distance)") @RequestParam(required = false) String sort
    ) {
        KakaoLocalSearchResponse response = mapService.search(query, x, y, size, sort);
        return BaseResponse.ok(response);
    }

    @Operation(summary = "장소 상세 정보 조회", description = "placeId를 이용해 장소 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "장소를 찾을 수 없음")
    })
    @GetMapping("/{placeId}")
    public BaseResponse<PlaceDetailResponse> getPlaceDetail(
            @Parameter(description = "장소 ID", required = true) @PathVariable Long placeId
    ) {
        PlaceDetailResponse response = mapService.getPlaceDetail(placeId);
        return BaseResponse.ok(response);
    }

    @Operation(summary = "장소 리뷰 조회", description = "placeId를 이용해 해당 장소의 모든 리뷰를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/{placeId}/reviews")
    public BaseResponse<PagedReviewResponse> getReviewsByPlace(
            @Parameter(description = "장소 ID", required = true) @PathVariable Long placeId,
            @Parameter(description = "페이지 번호") @RequestParam(defaultValue = "0") int page
    ) {
        return BaseResponse.ok(mapService.getReviewsByPlace(placeId, page));
    }

    @Operation(summary = "사용자 리뷰 조회", description = "사용자가 작성한 모든 리뷰를 조회합니다.")
    @GetMapping("/reviews/myReviews")
    public BaseResponse<PagedReviewResponse> getMyReviews(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page
    ) {
        return BaseResponse.ok(mapService.getMyReviews(userDetails.getId(), page));
    }

    @Operation(summary = "리뷰 상세 조회", description = "reviewId를 이용해 특정 리뷰 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "리뷰를 찾을 수 없음")
    })
    @GetMapping("/reviews/{reviewId}")
    public BaseResponse<PlaceReviewResponse> getReviewById(
            @Parameter(description = "리뷰 ID", required = true) @PathVariable Long reviewId
    ) {
        return BaseResponse.ok(mapService.getReviewById(reviewId));
    }

    @Operation(summary = "장소 리뷰 작성", description = "placeId에 해당하는 장소에 리뷰를 작성합니다.")
    @ApiResponse(responseCode = "201", description = "작성 성공",
            content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @PostMapping("/{placeId}/reviews")
    public BaseResponse<PlaceReviewResponse> createReview(
            @Parameter(description = "인증된 사용자 ID", hidden = true)
            @AuthenticationPrincipal CustomUserDetails userDetails,  // ✅ 로그인한 사용자의 정보 가져오기
            @Parameter(description = "장소 ID", required = true)
            @PathVariable Long placeId,
            @RequestBody @Valid CreateReviewRequest request
    ) {
        return BaseResponse.created(
                mapService.createReview(placeId, userDetails.getId(), request)
        );
    }

    @Operation(summary = "리뷰 수정", description = "reviewId를 이용해 리뷰를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "수정 성공",
            content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @PatchMapping("/reviews/{reviewId}")
    public BaseResponse<PlaceReviewResponse> updateReview(
            @Parameter(description = "인증된 사용자 ID", hidden = true)
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Parameter(description = "리뷰 ID", required = true)
            @PathVariable Long reviewId,
            @RequestBody @Valid UpdateReviewRequest request
    ) {
        return BaseResponse.ok(
                mapService.updateReview(reviewId, userDetails.getId(), request)
        );
    }

    @Operation(summary = "리뷰 삭제", description = "reviewId를 이용해 리뷰를 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "삭제 성공",
            content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @DeleteMapping("/reviews/{reviewId}")
    public BaseResponse<PlaceReviewDeleteResponse> deleteReview(
            @Parameter(description = "인증된 사용자 ID", hidden = true)
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Parameter(description = "리뷰 ID", required = true)
            @PathVariable Long reviewId
    ) {
        return BaseResponse.ok(
                mapService.deleteReview(reviewId, userDetails.getId())
        );
    }
}