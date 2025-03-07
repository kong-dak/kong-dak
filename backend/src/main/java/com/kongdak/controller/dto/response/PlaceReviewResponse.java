package com.kongdak.controller.dto.response;

import com.kongdak.domain.map.PlaceReview;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "장소 리뷰 정보")
public record PlaceReviewResponse(
        @Schema(description = "작성자 닉네임")
        String nickname,
        @Schema(description = "별점")
        int rating,
        @Schema(description = "리뷰 내용")
        String comment
) {
    public static PlaceReviewResponse from(PlaceReview review) {
        return new PlaceReviewResponse(
                review.getMember().getNickname(),
                review.getRating(),
                review.getComment()
        );
    }
}
