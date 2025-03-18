package com.kongdak.domain.calendar.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kongdak.global.exception.BusinessException;
import com.kongdak.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "일정 카테고리")
public enum ScheduleCategory {
    @Schema(description = "개인 일정")
    PERSONAL("개인 일정"),

    @Schema(description = "공유 일정")
    SHARED("공유 일정"),

    @Schema(description = "나만 보기")
    PRIVATE("나만 보기");

    private final String description;

    @JsonValue // JSON 직렬화 시 name() 대신 description을 사용
    public String getValue() {
        return description;
    }

    @JsonCreator // JSON 역직렬화 시 사용
    public static ScheduleCategory from(String value) {
        for (ScheduleCategory category : ScheduleCategory.values()) {
            if (category.name().equalsIgnoreCase(value) ||
                    category.getDescription().equals(value)) {
                return category;
            }
        }
        throw new BusinessException(ErrorCode.INVALID_SCHEDULE_CATEGORY);
    }
}

