package com.kongdak.controller.dto.request;

import com.kongdak.domain.member.OAuthProvider;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 생성 요청")
public record MemberCreateRequest(
        @Schema(description = "이메일", example = "user@example.com")
        String email,

        @Schema(description = "닉네임", example = "홍길동")
        String nickname,

        @Schema(description = "OAuth 제공자", example = "KAKAO")
        OAuthProvider oauthProvider
) {}
