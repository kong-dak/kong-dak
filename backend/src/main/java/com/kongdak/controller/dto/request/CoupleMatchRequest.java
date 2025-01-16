package com.kongdak.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "커플 매칭 요청")
public record CoupleMatchRequest(
        @Schema(description = "요청자 ID", example = "1")
        Long requesterId,

        @Schema(description = "대상자 ID", example = "2")
        Long targetId
) {
}
