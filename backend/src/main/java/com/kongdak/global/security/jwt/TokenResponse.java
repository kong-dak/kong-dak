package com.kongdak.global.security.jwt;

import lombok.Builder;

@Builder
public record TokenResponse(
        String accessToken,
        String refreshToken,
        String provider
) {}
