package com.kongdak.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "회원 복구 응답")
public record ActivateResponse(
        @Schema(description = "복구 처리된 이메일", example = "user@example.com")
        String email,

        @Schema(description = "복구 처리 시간", example = "2024-01-19T18:30:00")
        LocalDateTime ActivatedAt
) {
}
