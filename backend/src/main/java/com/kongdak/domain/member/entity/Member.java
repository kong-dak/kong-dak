package com.kongdak.domain.member.entity;

import com.kongdak.domain.BaseTimeEntity;
import com.kongdak.domain.couple.entity.Couple;
import com.kongdak.global.exception.BusinessException;
import com.kongdak.global.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "members")
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "oauth_provider", nullable = false)
    private OAuthProvider oauthProvider;

    @Column(nullable = false)
    private boolean isActive = true;

    @Column(nullable = false)
    @Setter
    private boolean nicknameSet = false;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "couple_id")
    private Couple couple;

    @Builder
    public Member(String email, String nickname, OAuthProvider oAuthProvider) {
        this.email = email;
        this.nickname = nickname;
        this.oauthProvider = oAuthProvider;
    }

    public void updateNickname(String nickname) {
        validateNickname(nickname);
        this.nickname = nickname;
    }

    private void validateNickname(String nickname) {
        if (nickname == null || nickname.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_NICKNAME);
        }
        if (nickname.length() < 2 || nickname.length() > 20) {
            throw new BusinessException(ErrorCode.INVALID_NICKNAME_LENGTH);
        }
    }

    public Long getCoupleId() {
        return couple != null ? couple.getId() : null;
    }

    public void deactivate(){
        this.isActive = false;
    }

    public void activate(){
        this.isActive = true;
    }

}
