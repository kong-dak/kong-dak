package com.kongdak.controller.dto.response;

import lombok.Builder;

@Builder
public record TokenRefreshResponse(
        String accessToken,
        String refreshToken
) {
}
