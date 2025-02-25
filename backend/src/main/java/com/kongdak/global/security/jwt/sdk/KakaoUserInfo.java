package com.kongdak.global.security.jwt.sdk;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoUserInfo(
        Long id,
        @JsonProperty("connected_at") String connectedAt,
        @JsonProperty("kakao_account") KakaoAccount kakaoAccount
) {
}
