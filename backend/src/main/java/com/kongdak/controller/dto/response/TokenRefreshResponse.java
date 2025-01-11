package com.kongdak.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "토큰 갱신 응답")
@Builder
public record TokenRefreshResponse(
        @Schema(description = "액세스 토큰", example = "eyJhbGciOiJIUzI1NiIs...")
        String accessToken,

        @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1NiIs...")
        String refreshToken
) {}
