package com.kongdak.domain.calendar.dto.response;
import com.kongdak.domain.calendar.entity.Schedule;
import com.kongdak.domain.calendar.entity.ScheduleCategory;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
@Schema(description = "일정 응답")
public record ScheduleResponse(
        @Schema(description = "일정 ID", example = "1")
        Long scheduleId,

        @Schema(description = "작성자 ID", example = "1")
        Long memberId,

        @Schema(description = "제목", example = "점심 약속")
        String title,

        @Schema(description = "시작 시간", example = "2024-01-10T12:00:00")
        LocalDateTime startTime,

        @Schema(description = "종료 시간", example = "2024-01-10T13:00:00")
        LocalDateTime endTime,

        @Schema(description = "설명", example = "친구와 점심 약속")
        String description,

        @Schema(description = "카테고리", example = "PERSONAL")
        ScheduleCategory category,

        @Schema(description = "이모지", example = "🍽️")
        String emoji,

        @Schema(description = "공휴일 여부", example = "false")
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

