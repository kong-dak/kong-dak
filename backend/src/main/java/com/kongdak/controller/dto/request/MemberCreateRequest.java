package com.kongdak.controller.dto.request;

import com.kongdak.domain.member.OAuthProvider;

public record MemberCreateRequest(
    String email,
    String nickname,
    OAuthProvider oauthProvider) {
}
