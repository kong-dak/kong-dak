package com.kongdak.domain.calendar.event;

import com.kongdak.global.event.DomainEvent;

import java.time.LocalDateTime;

public record ScheduleUpdatedEvent(
        Long scheduleId,
        Long calendarId,
        Long updaterId,
        Long partnerId,
        String title,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String category
) implements DomainEvent {

    @Override
    public String getEventType() {
        return "SCHEDULE_UPDATED";
    }
}