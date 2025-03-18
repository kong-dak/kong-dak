package com.kongdak.domain.couple.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "커플 매칭 코드 응답")
public record CoupleMatchCodeResponse(
        @Schema(description = "연결 코드", example = "123456")
        String code
) {
}