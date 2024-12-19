package com.kongdak.controller.dto;

import com.kongdak.domain.member.OAuthProvider;

public record MemberCreateRequest(
    String email,
    String nickname,
    OAuthProvider oauthProvider) {
}
