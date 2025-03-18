package com.kongdak.domain.couple.event;

import com.kongdak.global.event.DomainEvent;

import java.time.LocalDateTime;

public record CoupleAnniversaryEvent(
        Long coupleId,
        Long user1Id,
        Long user2Id,
        String anniversaryName,
        LocalDateTime anniversaryDate,
        int daysRemaining
) implements DomainEvent {

    @Override
    public String getEventType() {
        return "COUPLE_ANNIVERSARY";
    }
}
