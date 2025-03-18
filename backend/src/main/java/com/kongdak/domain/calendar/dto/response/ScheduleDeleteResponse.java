package com.kongdak.domain.calendar.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "일정 삭제 응답")
public record ScheduleDeleteResponse(
        @Schema(description = "삭제된 일정 ID", example = "1")
        Long scheduleId,

        @Schema(description = "캘린더 ID", example = "1")
        Long calendarId,

        @Schema(description = "삭제된 일정 제목", example = "데이트")
        String scheduleTitle,

        @Schema(description = "삭제 시간", example = "2024-01-19T18:30:00")
        LocalDateTime deletedAt
) {
    public static ScheduleDeleteResponse of(
            Long scheduleId,
            Long calendarId,
            String scheduleTitle,
            LocalDateTime deletedAt) {
        return new ScheduleDeleteResponse(scheduleId, calendarId, scheduleTitle, deletedAt);
    }
}