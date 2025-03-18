package com.kongdak.domain.calendar.event;

import com.kongdak.global.event.DomainEvent;

import java.time.LocalDateTime;

public record ScheduleCreatedEvent(
        Long scheduleId,
        Long calendarId,
        Long creatorId,
        Long partnerId,
        String title,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String category
) implements DomainEvent {

    @Override
    public String getEventType() {
        return "SCHEDULE_CREATED";
    }
}
