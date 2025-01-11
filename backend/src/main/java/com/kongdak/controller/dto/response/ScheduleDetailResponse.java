package com.kongdak.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kongdak.domain.calendar.Schedule;
import com.kongdak.domain.calendar.ScheduleCategory;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
@Schema(description = "일정 상세 조회 응답")
public record ScheduleDetailResponse(
        @Schema(description = "일정 ID", example = "1")
        Long scheduleId,
        @Schema(description = "작성자 ID", example = "1")
        Long memberId,
        @Schema(description = "일정 제목", example = "미팅")
        String title,
        @Schema(description = "시작 시간", example = "2024-01-10T09:00:00")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime startTime,
        @Schema(description = "종료 시간", example = "2024-01-10T10:00:00")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime endTime,

        @Schema(description = "일정 설명", example = "팀 회의")
        String description,

        @Schema(description = "일정 카테고리", example = "개인 일정",
                allowableValues = {"개인 일정", "공유 일정", "나만 보기"})
        ScheduleCategory category,

        @Schema(description = "이모지", example = "📅")
        String emoji,

        @Schema(description = "공휴일 여부", example = "false")
        Boolean isHoliday,

        @Schema(description = "작성자 닉네임", example = "꼬미")
        String creatorNickname,

        @Schema(description = "생성 일자", example = "2024-01-10T09:00:00")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime createdAt,

        @Schema(description = "수정 일자", example = "2024-01-10T09:00:00")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime updatedAt
) {
    public static ScheduleDetailResponse from(Schedule schedule) {
        return new ScheduleDetailResponse(
                schedule.getId(),
                schedule.getCreator().getId(),
                schedule.getTitle(),
                schedule.getStartTime(),
                schedule.getEndTime(),
                schedule.getDescription(),
                schedule.getCategory(),
                schedule.getEmoji(),
                schedule.isHoliday(),
                schedule.getCreator().getNickname(),
                schedule.getCreatedAt(),
                schedule.getUpdatedAt()
        );
    }
}
