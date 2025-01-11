package com.kongdak.controller.dto.response;

import com.kongdak.domain.calendar.Calendar;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "캘린더 응답")
public record CalendarResponse(
        @Schema(description = "캘린더 ID", example = "1")
        Long calendarId,

        @Schema(description = "커플 ID", example = "1")
        Long coupleId,

        @Schema(description = "생성 시간", example = "2024-01-10T12:00:00")
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
