package com.kongdak.domain.map.dto.response;

import com.kongdak.domain.map.PlaceImage;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "장소 리뷰 이미지 정보")
public record PlaceImageResponse(
        @Schema(description = "이미지 ID", example = "1")
        Long id,
        @Schema(description = "이미지 URL")
        String imageUrl,
        @Schema(description = "이미지 설명")
        String description
) {
    public static PlaceImageResponse from(PlaceImage placeImage) {
        return new PlaceImageResponse(
                placeImage.getId(),
                placeImage.getImageUrl(),
                placeImage.getDescription()
        );
    }
}
