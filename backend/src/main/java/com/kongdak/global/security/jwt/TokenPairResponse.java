package com.kongdak.global.security.jwt;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "토큰 재발급 요청")
public record TokenPairResponse(

        @Schema(description = "Access 토큰", example = "eyJhbGciOiJIUzI1NiIs...")
        String accessToken,
        @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1NiIs...")
        String refreshToken
) {
}
