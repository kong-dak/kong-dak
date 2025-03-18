package com.kongdak.domain.couple.event;

import com.kongdak.global.event.DomainEvent;

public record CoupleMatchRequestedEvent(
        String requestId,
        Long requesterId,
        Long receiverId
) implements DomainEvent {

    @Override
    public String getEventType() {
        return "COUPLE_MATCH_REQUESTED";
    }
}

