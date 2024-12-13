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

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Couple extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member1_id")
    private Member member1;

    @OneToOne
    @JoinColumn(name = "member2_id")
    private Member member2;

    @Column(nullable = false)
    private LocalDateTime connectedAt;

    private LocalDateTime anniversaryDate;

    @Column(nullable = false)
    private boolean isConnected = true;

    private LocalDateTime disconnectedAt;

    @Builder
    public Couple(Member member1, Member member2, LocalDateTime anniversaryDate) {
        this.member1 = member1;
        this.member2 = member2;
        this.anniversaryDate = anniversaryDate;
    }

    // 파트너 ID를 가져오는 메서드
    public Long getPartnerId(Long memberId) {
        if (member1.getId().equals(memberId)) {
            return member2.getId();
        }
        return member1.getId();
    }

    public void disconnect(){
        this.isConnected = false;
        this.disconnectedAt = LocalDateTime.now();
    }

    public void restore(){
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
}
