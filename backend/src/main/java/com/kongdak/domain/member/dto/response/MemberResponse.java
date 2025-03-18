package com.kongdak.domain.member.dto.response;

import com.kongdak.domain.couple.dto.response.CoupleInfo;
import com.kongdak.domain.member.entity.Member;
import com.kongdak.domain.member.entity.OAuthProvider;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
@Schema(description = "회원 정보 응답")
public record MemberResponse(
        @Schema(description = "회원 ID", example = "1")
        Long memberId,

        @Schema(description = "닉네임", example = "홍길동")
        String nickname,

        @Schema(description = "파트너 닉네임", example = "토리")
        String partnerNickname,

        @Schema(description = "이메일", example = "user@example.com")
        String email,

        @Schema(description = "OAuth 제공자", example = "KAKAO")
        OAuthProvider oauthProvider,

        @Schema(description = "계정 생성일", example = "2024-01-10T12:00:00")
        LocalDateTime createdAt,

        @Schema(description = "계정 활성화 상태", example = "true")
        boolean isActive,

        @Schema(description = "커플 정보") CoupleInfo coupleInfo

) {
    public static MemberResponse from(Member member, Member partner){
        return new MemberResponse(
            member.getId(),
                member.getNickname(),
                partner != null ? partner.getNickname() : null,
                member.getEmail(),
                member.getOauthProvider(),
                member.getCreatedAt(),
                member.isActive(),
                member.getCouple() != null ? CoupleInfo.from(member.getCouple(), partner.getId()) : null
        );
    }
}
