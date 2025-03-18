package com.kongdak.domain.couple.event;

import com.kongdak.global.event.DomainEvent;

public record CoupleMatchRejectedEvent(
        String requestId,
        Long requesterId,  // 요청한 사람 ID
        Long receiverId    // 거절한 사람 ID
) implements DomainEvent {

    @Override
    public String getEventType() {
        return "COUPLE_MATCH_REJECTED";
    }
}
