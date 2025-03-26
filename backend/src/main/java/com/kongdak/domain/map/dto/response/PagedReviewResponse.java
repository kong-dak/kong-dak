package com.kongdak.domain.map.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

public record PagedReviewResponse(
        @Schema(description = "리뷰 리스트")
        List<PlaceReviewResponse> content,
        @Schema(description = "현재 페이지")
        int currentPage,
        @Schema(description = "전체 페이지 수")
        int totalPages,
        @Schema(description = "다음 페이지 존재 여부")
        boolean hasNext
) {
    public static PagedReviewResponse from(Page<PlaceReviewResponse> page) {
        return new PagedReviewResponse(
                page.getContent(),
                page.getNumber(),
                page.getTotalPages(),
                page.hasNext()
        );
    }
}
