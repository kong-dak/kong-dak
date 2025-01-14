package com.kongdak.controller;

import com.kongdak.controller.dto.request.ScheduleCreateRequest;
import com.kongdak.controller.dto.response.MonthlyScheduleResponse;
import com.kongdak.controller.dto.response.ScheduleDetailResponse;
import com.kongdak.controller.dto.response.ScheduleResponse;
import com.kongdak.domain.calendar.CalendarService;
import com.kongdak.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calendars")
@Tag(name = "캘린더", description = "캘린더 관련 API")
public class CalendarController {
    private final CalendarService calendarService;

    @Operation(
            summary = "월간 일정 조회",
            description = "해당 월의 모든 일정을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "월간 일정 조회 성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))
            )
    })
    @GetMapping("/{calendarId}/schedules/monthly")
    public BaseResponse<MonthlyScheduleResponse> getMonthlySchedules(
            @PathVariable Long calendarId,
            @RequestParam("datetime") @DateTimeFormat(pattern = "yyyyMM") LocalDateTime dateTime) {
        MonthlyScheduleResponse monthlySchedules = calendarService.getMonthlySchedules(calendarId, dateTime);
        return BaseResponse.ok(monthlySchedules);
    }

    @Operation(
            summary = "일간 일정 조회",
            description = "특정 날짜의 모든 일정을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "일간 일정 조회 성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))
            )
    })
    @GetMapping("/{calendarId}/schedules/daily")
    public BaseResponse<List<ScheduleResponse>> getDailySchedules(
            @PathVariable Long calendarId,
            @RequestParam("datetime") @DateTimeFormat(pattern = "yyyyMMdd") LocalDateTime dateTime) {
        return BaseResponse.ok(
                calendarService.getDailySchedules(calendarId, dateTime)
        );
    }

    @Operation(
            summary = "일정 상세 조회",
            description = "특정 일정의 상세 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "일정 상세 조회 성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))
            )
    })
    @GetMapping("/{calendarId}/schedules/{scheduleId}")
    public BaseResponse<ScheduleDetailResponse> getScheduleDetail(
            @PathVariable Long calendarId,
            @PathVariable Long scheduleId) {
        ScheduleDetailResponse scheduleDetail = calendarService.getScheduleDetail(calendarId, scheduleId);
        return BaseResponse.ok(scheduleDetail);
    }


    @Operation(
            summary = "일정 생성",
            description = "새로운 일정을 생성합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "일정 생성 성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))
            )
    })
    @PostMapping("/{calendarId}/schedules")
    public BaseResponse<ScheduleResponse> createSchedule(
            @Parameter(description = "캘린더 ID", example = "1")
            @PathVariable("calendarId") Long calendarId,
            @Valid @RequestBody ScheduleCreateRequest request) {
        ScheduleResponse response = calendarService.createSchedule(calendarId, request);
        return BaseResponse.created(response);
    }


    @Operation(
            summary = "일정 수정",
            description = "기존 일정을 수정합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "일정 수정 성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))
            )
    })
    @PatchMapping("/{calendarId}/schedules/{scheduleId}")
    public BaseResponse<ScheduleResponse> updateSchedule(
            @PathVariable Long calendarId,
            @PathVariable Long scheduleId,
            @Valid @RequestBody ScheduleCreateRequest request) {
        ScheduleResponse scheduleResponse = calendarService.updateSchedule(calendarId, scheduleId, request);
        return BaseResponse.ok(scheduleResponse);
    }

    @Operation(
            summary = "일정 삭제",
            description = "특정 일정을 삭제합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "일정 삭제 성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))
            )
    })
    @DeleteMapping("/{calendarId}/schedules/{scheduleId}")
    public BaseResponse<Void> deleteSchedule(
            @PathVariable Long calendarId,
            @PathVariable Long scheduleId) {
        calendarService.deleteSchedule(calendarId, scheduleId);
        return BaseResponse.ok();
    }
}
