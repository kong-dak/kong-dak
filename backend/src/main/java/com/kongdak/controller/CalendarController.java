package com.kongdak.controller;

import com.kongdak.controller.dto.request.ScheduleCreateRequest;
import com.kongdak.controller.dto.response.MonthlyScheduleResponse;
import com.kongdak.controller.dto.response.ScheduleDeleteResponse;
import com.kongdak.controller.dto.response.ScheduleDetailResponse;
import com.kongdak.controller.dto.response.ScheduleResponse;
import com.kongdak.domain.calendar.CalendarService;
import com.kongdak.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
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
    @GetMapping("/schedules/monthly")
    public BaseResponse<MonthlyScheduleResponse> getMonthlySchedules(
            @RequestParam("datetime") @DateTimeFormat(pattern = "yyyyMM") YearMonth dateTime) {
        MonthlyScheduleResponse monthlySchedules = calendarService.getMonthlySchedules(dateTime);
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
    @GetMapping("/schedules/daily")
    public BaseResponse<List<ScheduleResponse>> getDailySchedules(

            @RequestParam("datetime")
            @DateTimeFormat(pattern = "yyyyMMdd") LocalDate dateTime) {
        return BaseResponse.ok(
                calendarService.getDailySchedules(dateTime)
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
    @GetMapping("/schedules/{scheduleId}")
    public BaseResponse<ScheduleDetailResponse> getScheduleDetail(
            @PathVariable("scheduleId") Long scheduleId) {
        ScheduleDetailResponse scheduleDetail = calendarService.getScheduleDetail(scheduleId);
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
    @PostMapping("/schedules")
    public BaseResponse<ScheduleResponse> createSchedule(
            @Valid @RequestBody ScheduleCreateRequest request) {
        return BaseResponse.created(calendarService.createSchedule(request));
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
    @PatchMapping("/schedules/{scheduleId}")
    public BaseResponse<ScheduleResponse> updateSchedule(
            @PathVariable("scheduleId") Long scheduleId,
            @Valid @RequestBody ScheduleCreateRequest request) {
        ScheduleResponse scheduleResponse = calendarService.updateSchedule(scheduleId, request);
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
    @DeleteMapping("/schedules/{scheduleId}")
    public BaseResponse<ScheduleDeleteResponse> deleteSchedule(
            @PathVariable("scheduleId") Long scheduleId) {
        return BaseResponse.ok(calendarService.deleteSchedule(scheduleId));
    }
}
