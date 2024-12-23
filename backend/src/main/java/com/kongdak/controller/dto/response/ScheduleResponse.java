package com.kongdak.controller.dto.response;
import com.kongdak.domain.calendar.Schedule;
import com.kongdak.domain.calendar.ScheduleCategory;

import java.time.LocalDateTime;
public record ScheduleResponse(
        Long scheduleId,
        Long memberId,
        String title,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String description,
        ScheduleCategory category,
        String emoji,
        boolean isHoliday
) {
    public static ScheduleResponse from(Schedule schedule) {
        return new ScheduleResponse(
                schedule.getId(),
                schedule.getCreator().getId(),
                schedule.getTitle(),
                schedule.getStartTime(),
                schedule.getEndTime(),
                schedule.getDescription(),
                schedule.getCategory(),
                schedule.getEmoji(),
                schedule.isHoliday()
        );
    }
}

