package com.kongdak.controller.dto.response;

import com.kongdak.domain.calendar.Holiday;

import java.time.LocalDateTime;

public record HolidayResponse(
        String name,
        LocalDateTime date,
        String description
) {
    public static HolidayResponse from(Holiday holiday) {
        return new HolidayResponse(
                holiday.getName(),
                holiday.getDate().atStartOfDay(),
                holiday.getDescription()
        );
    }
}
