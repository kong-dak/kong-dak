package com.kongdak.domain.member.dto.response;

public record MemberNicknameChangeResponse(
        String email,
        String nickname
) {
}
