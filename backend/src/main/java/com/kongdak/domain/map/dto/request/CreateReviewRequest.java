package com.kongdak.domain.map.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "리뷰 생성 요청")
public record CreateReviewRequest(
        @Schema(description = "별점", example = "5")
        @NotNull
        int rating,

        @Schema(description = "리뷰 내용", example = "이 장소 정말 좋았어요!")
        @NotNull
        String comment,

        @Schema(description = "첨부된 이미지 URL 목록", example = "[\"photo1.jpg\", \"photo2.jpg\"]")
        List<String> imageUrls
) {}
