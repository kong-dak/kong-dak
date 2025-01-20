package com.kongdak.controller.dto.response;

import com.kongdak.domain.member.Member;
import com.kongdak.domain.member.MemberRepository;
import com.kongdak.domain.member.OAuthProvider;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
@Schema(description = "회원 정보 응답")
public record MemberResponse(
        @Schema(description = "회원 ID", example = "1")
        Long memberId,

        @Schema(description = "닉네임", example = "홍길동")
        String nickname,

        @Schema(description = "이메일", example = "user@example.com")
        String email,

        @Schema(description = "OAuth 제공자", example = "KAKAO")
        OAuthProvider oauthProvider,

        @Schema(description = "계정 생성일", example = "2024-01-10T12:00:00")
        LocalDateTime createdAt,

        @Schema(description = "계정 활성화 상태", example = "true")
        boolean isActive,

        @Schema(description = "커플 정보")
        CoupleInfo coupleInfo

) {
    public static MemberResponse from(Member member, Member partner){
        return new MemberResponse(
            member.getId(),
                member.getNickname(),
                member.getEmail(),
                member.getOauthProvider(),
                member.getCreatedAt(),
                member.isActive(),
                member.getCouple() != null ? CoupleInfo.from(member.getCouple(), partner.getId()) : null
        );
    }
}
