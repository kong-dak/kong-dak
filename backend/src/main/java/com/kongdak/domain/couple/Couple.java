package com.kongdak.domain.couple;

import com.kongdak.domain.BaseTimeEntity;
import com.kongdak.domain.member.Member;
import com.kongdak.global.exception.BusinessException;
import com.kongdak.global.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "couples")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Couple extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "couple")
    private List<Member> members = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime connectedAt;

    private LocalDateTime anniversaryDate;

    @Column(nullable = false)
    private boolean isConnected = true;

    private LocalDateTime disconnectedAt;

    @Builder
    public Couple(Member member1, Member member2, LocalDateTime anniversaryDate) {
        validateMemberNotNull(member1, member2);
        this.connectedAt = LocalDateTime.now();
        this.isConnected = true;
        this.anniversaryDate = anniversaryDate;
        connectMembers(member1, member2);
    }

    private void validateMemberNotNull(Member member1, Member member2) {
        if (member1 == null || member2 == null) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
        }
    }

    // 연관관계 편의 메서드
    private void connectMembers(Member member1, Member member2) {
        validateMemberSize();
        this.members.add(member1);
        this.members.add(member2);
    }

    // 커플 멤버 수 검증
    private void validateMemberSize() {
        if (!members.isEmpty()) {
            throw new BusinessException(ErrorCode.COUPLE_ALREADY_EXISTS);
        }
    }

    // 파트너 ID를 가져오는 메서드
    public Long getPartnerId(Long memberId) {
        return members.stream()
                .filter(member -> !member.getId().equals(memberId))
                .map(Member::getId)
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.PARTNER_NOT_FOUND));
    }

    public void disconnect() {
        validateConnected();
        this.isConnected = false;
        this.disconnectedAt = LocalDateTime.now();
    }

    public void restore() {
        validateCanRestore();
        this.isConnected = true;
        this.disconnectedAt = null;
    }

    public boolean canRestore() {
        if (disconnectedAt == null) {
            return false;
        }
        return ChronoUnit.DAYS.between(disconnectedAt, LocalDateTime.now()) <= 50;
    }

    private void validateCanRestore() {
        if (!canRestore()) {
            throw new BusinessException(ErrorCode.CANNOT_RESTORE_COUPLE);
        }
    }

    private void validateConnected() {
        if (!isConnected) {
            throw new BusinessException(ErrorCode.COUPLE_ALREADY_DISCONNECTED);
        }
    }

    // 멤버가 이 커플에 속해있는지 확인
    public boolean containsMember(Long memberId) {
        return members.stream()
                .anyMatch(member -> member.getId().equals(memberId));
    }
}
