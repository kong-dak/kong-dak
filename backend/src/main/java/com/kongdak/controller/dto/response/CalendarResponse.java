package com.kongdak.controller.dto.response;

import com.kongdak.domain.calendar.Calendar;

import java.time.LocalDateTime;

public record CalendarResponse(
        Long calendarId,
        Long coupleId,
        LocalDateTime createdAt
) {
    public static CalendarResponse from(Calendar calendar) {
        return new CalendarResponse(
                calendar.getId(),
                calendar.getCouple().getId(),
                calendar.getCreatedAt()
        );
    }
}
