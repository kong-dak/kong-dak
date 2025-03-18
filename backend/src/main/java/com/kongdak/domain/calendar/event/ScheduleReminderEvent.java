package com.kongdak.domain.calendar.event;

import com.kongdak.global.event.DomainEvent;

import java.time.LocalDateTime;

public record ScheduleReminderEvent(
        Long scheduleId,
        Long receiverId,
        String title,
        LocalDateTime startTime,
        int minutesRemaining
) implements DomainEvent {

    @Override
    public String getEventType() {
        return "SCHEDULE_REMINDER";
    }
}
