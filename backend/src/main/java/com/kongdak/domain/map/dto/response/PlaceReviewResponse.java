package com.kongdak.domain.map.dto.response;

import com.kongdak.domain.map.PlaceReview;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "장소 리뷰 정보")
public record PlaceReviewResponse(
        @Schema(description = "작성자 닉네임")
        String nickname,
        @Schema(description = "별점")
        int rating,
        @Schema(description = "리뷰 내용")
        String comment,
        @Schema(description = "리뷰 이미지")
        List<PlaceImageResponse> images,
        @Schema(description = "작성 시간", example = "2024-01-10T12:00:00")
        LocalDateTime createdAt,
        @Schema(description = "수정 시간", example = "2024-01-10T12:30:00")
        LocalDateTime updatedAt
) {
    public static PlaceReviewResponse from(PlaceReview review) {
        return new PlaceReviewResponse(
                review.getMember().getNickname(),
                review.getRating(),
                review.getComment(),
                review.getPlaceImages().stream()
                        .map(PlaceImageResponse::from)
                        .toList(),
                review.getCreatedAt(),
                review.getUpdatedAt()
        );
    }
}
