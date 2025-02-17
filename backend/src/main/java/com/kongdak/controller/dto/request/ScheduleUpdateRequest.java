package com.kongdak.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kongdak.domain.calendar.ScheduleCategory;
import com.kongdak.global.exception.BusinessException;
import com.kongdak.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Schema(description = "일정 수정 요청")
public record ScheduleUpdateRequest(
        @Schema(
                description = "일정 제목",
                example = "점심 약속",
                maxLength = 30
        )
        @NotBlank(message = "제목은 필수입니다")
        @Size(max = 30, message = "제목은 30자를 초과할 수 없습니다")
        String title,

        @Schema(
                description = "일정 시작 시간",
                example = "2024-01-10 12:00",
                pattern = "yyyy-MM-dd HH:mm"
        )
        @NotNull(message = "시작 시간은 필수입니다")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime startTime,

        @Schema(
                description = "일정 종료 시간",
                example = "2024-01-10 13:00",
                pattern = "yyyy-MM-dd HH:mm"
        )
        @NotNull(message = "종료 시간은 필수입니다")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime endTime,

        @Schema(
                description = "일정 설명",
                example = "을지로 3가 맛집에서 친구와 점심 약속",
                maxLength = 500
        )
        @Size(max = 500, message = "설명은 500자를 초과할 수 없습니다")
        String description,

        @Schema(
                description = "일정 카테고리",
                example = "PERSONAL",
                allowableValues = {"PERSONAL", "SHARED", "PRIVATE"}
        )
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

