package com.kongdak.domain.calendar.dto.response;

import com.kongdak.domain.calendar.entity.Holiday;
import com.kongdak.domain.calendar.entity.Schedule;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "월간 일정 및 공휴일 응답")
public record MonthlyScheduleResponse(
        @Schema(
                description = "월간 일정 목록",
                example = """
                [
                    {
                        "scheduleId": 1,
                        "title": "점심 약속",
                        "startTime": "2024-01-10 12:00",
                        "endTime": "2024-01-10 13:00",
                        "category": "PERSONAL",
                        "isHoliday": false
                    }
                ]
                """
        )
        List<ScheduleResponse> schedules,
        @Schema(
                description = "공휴일 목록",
                example = """
                [
                    {
                        "name": "설날",
                        "date": "2024-02-10",
                        "isLunar": true
                    }
                ]
                """
        )
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

