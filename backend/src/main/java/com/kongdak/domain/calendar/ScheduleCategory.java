package com.kongdak.domain.calendar;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ScheduleCategory {
    PERSONAL("개인 일정"),
    SHARED("공유 일정"),
    PRIVATE("나만 보기");

    private final String description;
}

