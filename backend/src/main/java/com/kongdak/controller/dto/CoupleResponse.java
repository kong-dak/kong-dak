package com.kongdak.controller.dto;

import com.kongdak.domain.couple.Couple;

import java.time.LocalDateTime;

public record CoupleResponse(
        Long coupleId,
        Long partnerId,
        LocalDateTime connectedAt,
        LocalDateTime anniversaryDate,
        boolean isConnected
) {
    public static CoupleResponse from(Couple couple, Long memberId) {
        return new CoupleResponse(
                couple.getId(),
                couple.getPartnerId(memberId),
                couple.getConnectedAt(),
                couple.getAnniversaryDate(),
                couple.isConnected()
        );
    }
}
