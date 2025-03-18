package com.kongdak.domain.bucketlist.event;

import com.kongdak.global.event.DomainEvent;

public record BucketCreatedEvent(
        Long bucketId,
        Long creatorId,
        Long partnerId,
        String title,
        String category
) implements DomainEvent {

    @Override
    public String getEventType() {
        return "BUCKET_CREATED";
    }
}
