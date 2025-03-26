package com.kongdak.domain.map.dto.response;

import com.kongdak.domain.map.PlaceReview;

import java.time.LocalDateTime;

public record PlaceReviewDeleteResponse(
        Long reviewId,
        Long placeId,
        Long memberId,
        LocalDateTime deleteAt
) {
    public static PlaceReviewDeleteResponse of(PlaceReview review) {
        return new PlaceReviewDeleteResponse(
                review.getId(),
                review.getPlace().getId(),
                review.getMember().getId(),
                LocalDateTime.now()
        );
    }
}
