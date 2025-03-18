package com.kongdak.domain.member.entity;

import com.kongdak.global.exception.BusinessException;
import com.kongdak.global.exception.ErrorCode;

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
