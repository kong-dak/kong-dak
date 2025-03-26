package com.kongdak.domain.map.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "장소 상세 정보")
public record PlaceDetailResponse(
        @Schema(description = "장소 ID")
        Long placeId,
        @Schema(description = "장소 이름")
        String placeName,
        @Schema(description = "카테고리 종류")
        String categoryName,
        @Schema(description = "주소")
        String addressName,
        @Schema(description = "도로명 주소")
        String roadAddressName,
        @Schema(description = "전화 번호")
        String phone,
        @Schema(description = "미리보기 이미지")
        List<String> previewImages,
        @Schema(description = "미리보기 리뷰")
        List<PlaceReviewResponse> previewReviews,
        @Schema(description = "장소 운영 시간")
        List<PlaceOperatingHourResponse> operatingHours

) {
    public static PlaceDetailResponse of(
            Long placeId,
            String placeName,
            String categoryName,
            String addressName,
            String roadAddressName,
            String phone,
            List<String> previewImages,
            List<PlaceReviewResponse> previewReviews,
            List<PlaceOperatingHourResponse> operatingHours
    ) {
        return new PlaceDetailResponse(
                placeId, placeName, categoryName, addressName, roadAddressName, phone,
                previewImages, previewReviews, operatingHours
        );
    }
}
