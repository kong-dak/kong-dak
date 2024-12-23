package com.kongdak.controller.dto.response;

import com.kongdak.domain.calendar.Holiday;
import com.kongdak.domain.calendar.Schedule;

import java.util.List;
import java.util.stream.Collectors;

public record MonthlyScheduleResponse(
        List<ScheduleResponse> schedules,
        List<HolidayResponse> holidays
) {
    public static MonthlyScheduleResponse of(List<Schedule> schedules, List<Holiday> holidays) {
        return new MonthlyScheduleResponse(
                schedules.stream()
                        .map(ScheduleResponse::from)
                        .collect(Collectors.toList()),
                holidays.stream()
                        .map(HolidayResponse::from)
                        .collect(Collectors.toList())
        );
    }
}

