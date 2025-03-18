package com.kongdak.domain.couple.entity;

import com.kongdak.domain.BaseTimeEntity;
import com.kongdak.domain.member.entity.Member;
import com.kongdak.global.exception.BusinessException;
import com.kongdak.global.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Entity
@Getter
@Table(name = "couples")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Couple extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime connectedAt;

    private LocalDateTime anniversaryDate;

    @Column(nullable = false)
    private boolean isConnected = true;

    private LocalDateTime disconnectedAt;

    @Builder
    public Couple(LocalDateTime anniversaryDate) {
        this.connectedAt = LocalDateTime.now();
        this.isConnected = true;
        this.anniversaryDate = anniversaryDate;
    }

    private void validateMemberNotNull(List<Member> members) {
        if (members == null || members.size() != 2) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
        }
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

}
