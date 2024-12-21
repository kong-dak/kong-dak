package com.kongdak.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kongdak.domain.calendar.ScheduleCategory;
import com.kongdak.global.exception.BusinessException;
import com.kongdak.global.exception.ErrorCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record ScheduleCreateRequest(
        @NotBlank(message = "제목은 필수입니다")
        @Size(max = 30, message = "제목은 30자를 초과할 수 없습니다")
        String title,

        @NotNull(message = "시작 시간은 필수입니다")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime startTime,

        @NotNull(message = "종료 시간은 필수입니다")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime endTime,

        @Size(max = 500, message = "설명은 500자를 초과할 수 없습니다")
        String description,

        @NotNull(message = "일정 카테고리는 필수입니다")
        ScheduleCategory category,

        String emoji
) {
    public void validate() {
        if (startTime.isAfter(endTime)) {
            throw new BusinessException(ErrorCode.INVALID_SCHEDULE_TIME);
        }
    }
}
