package com.kongdak.domain.member;

import com.kongdak.global.exception.BusinessException;
import com.kongdak.global.exception.ErrorCode;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

public enum OAuthProvider {
    KAKAO, GOOGLE;

    public static OAuthProvider from(String registrationId) {
        return switch (registrationId.toLowerCase()) {
            case "kakao" -> KAKAO;
            case "google" -> GOOGLE;
            default -> throw new BusinessException(ErrorCode.INVALID_PROVIDER);
        };
    }
}
