package com.kongdak.domain.map.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "리뷰 수정 요청")
public record UpdateReviewRequest(
        @Schema(description = "별점", example = "4")
        @NotNull
        int rating,

        @Schema(description = "리뷰 내용", example = "수정된 리뷰 내용입니다.")
        @NotNull
        String comment,

        @Schema(description = "새로운 이미지 URL 목록 (추가할 이미지)", example = "[\"new_photo1.jpg\", \"new_photo2.jpg\"]")
        List<String> newImageUrls,

        @Schema(description = "삭제할 이미지 ID 목록", example = "[1, 2, 3]")
        List<Long> deleteImageIds
) {}
