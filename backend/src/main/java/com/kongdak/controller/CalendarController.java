package com.kongdak.controller;

import com.kongdak.controller.dto.request.ScheduleCreateRequest;
import com.kongdak.controller.dto.response.MonthlyScheduleResponse;
import com.kongdak.controller.dto.response.ScheduleDetailResponse;
import com.kongdak.controller.dto.response.ScheduleResponse;
import com.kongdak.domain.calendar.CalendarService;
import com.kongdak.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calendars")
public class CalendarController {
    private final CalendarService calendarService;

    @GetMapping("/{calendarId}/schedules/monthly")
    public ApiResponse<MonthlyScheduleResponse> getMonthlySchedules(
            @PathVariable Long calendarId,
            @RequestParam("datetime") @DateTimeFormat(pattern = "yyyyMM") LocalDateTime dateTime) {
        MonthlyScheduleResponse monthlySchedules = calendarService.getMonthlySchedules(calendarId, dateTime);
        return ApiResponse.ok(monthlySchedules);
    }

    @GetMapping("/{calendarId}/schedules/daily")
    public ApiResponse<List<ScheduleResponse>> getDailySchedules(
            @PathVariable Long calendarId,
            @RequestParam("datetime") @DateTimeFormat(pattern = "yyyyMMdd") LocalDateTime dateTime) {
        return ApiResponse.ok(
                calendarService.getDailySchedules(calendarId, dateTime)
        );
    }

    @GetMapping("/{calendarId}/schedules/{scheduleId}")
    public ApiResponse<ScheduleDetailResponse> getScheduleDetail(
            @PathVariable Long calendarId,
            @PathVariable Long scheduleId) {
        ScheduleDetailResponse scheduleDetail = calendarService.getScheduleDetail(calendarId, scheduleId);
        return ApiResponse.ok(scheduleDetail);
    }

    @PostMapping("/{calendarId}/schedules")
    public ApiResponse<ScheduleResponse> createSchedule(
            @PathVariable Long calendarId,
            @Valid @RequestBody ScheduleCreateRequest request) {
        ScheduleResponse response = calendarService.createSchedule(calendarId, request);
        return ApiResponse.created(response);
    }

    @PatchMapping("/{calendarId}/schedules/{scheduleId}")
    public ApiResponse<ScheduleResponse> updateSchedule(
            @PathVariable Long calendarId,
            @PathVariable Long scheduleId,
            @Valid @RequestBody ScheduleCreateRequest request) {
        ScheduleResponse scheduleResponse = calendarService.updateSchedule(calendarId, scheduleId, request);
        return ApiResponse.ok(scheduleResponse);
    }

    @DeleteMapping("/{calendarId}/schedules/{scheduleId}")
    public ApiResponse<Void> deleteSchedule(
            @PathVariable Long calendarId,
            @PathVariable Long scheduleId) {
        calendarService.deleteSchedule(calendarId, scheduleId);
        return ApiResponse.ok();
    }
}
