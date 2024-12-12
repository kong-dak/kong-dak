package com.kongdak.controller.dto;

import com.kongdak.domain.member.Member;
import com.kongdak.domain.member.OAuthProvider;

public record MemberResponse(
        Long memberId,
        String email,
        String nickname,
        OAuthProvider oAuthProvider,
        boolean isActive,
        CoupleInfo coupleInfo

) {
    public static MemberResponse from(Member member){
        return new MemberResponse(
            member.getId(),
                member.getEmail(),
                member.getNickname(),
                member.getOAuthProvider(),
                member.isActive(),
                member.getCouple() != null ? CoupleInfo.from(member.getCouple(), member.getId()) : null
        );
    }
}
