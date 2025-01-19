package com.kongdak.domain.calendar;

import com.kongdak.controller.dto.request.ScheduleCreateRequest;
import com.kongdak.controller.dto.response.*;
import com.kongdak.domain.couple.Couple;
import com.kongdak.domain.member.Member;
import com.kongdak.domain.member.MemberService;
import com.kongdak.global.exception.BusinessException;
import com.kongdak.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalendarService {
    private final CalendarRepository calendarRepository;
    private final ScheduleRepository scheduleRepository;
    private final HolidayRepository holidayRepository;
    private final MemberService memberService;

    // 캘린더 생성 (커플 연결 시 자동 생성)
    @Transactional
    public CalendarResponse createCalendar(Couple couple) {
        if (calendarRepository.existsByCouple(couple)) {
            throw new BusinessException(ErrorCode.DUPLICATE_CALENDAR);
        }

        Calendar calendar = Calendar.builder()
                .couple(couple)
                .build();
        Calendar savedCalendar = calendarRepository.save(calendar);
        return CalendarResponse.from(savedCalendar);
    }

    // 월별 일정 조회
    public MonthlyScheduleResponse getMonthlySchedules(Long calendarId, LocalDateTime dateTime) {
        Calendar calendar = findCalendarById(calendarId);

        validateCalendarAccess(calendar);

        int year = dateTime.getYear();
        int month = dateTime.getMonthValue();

        List<Schedule> schedules = scheduleRepository.findMonthlySchedules(calendarId, year, month);
        List<Holiday> holidays = holidayRepository.findByYearAndMonth(year, month);

        return MonthlyScheduleResponse.of(schedules, holidays);
    }

    // 일정 상세 조회
    public ScheduleDetailResponse getScheduleDetail(Long calendarId, Long scheduleId) {
        Calendar calendar = findCalendarById(calendarId);
        Schedule schedule = findScheduleById(scheduleId);

        validateCalendarAccess(calendar);
        validateScheduleForCalendar(schedule, calendarId);

        return ScheduleDetailResponse.from(schedule);
    }

    // 월별 휴일 조회
    public List<HolidayResponse> getMonthlyHolidays(LocalDateTime dateTime) {

        return holidayRepository.findByYearAndMonth(
                dateTime.getYear(),
                dateTime.getMonthValue())
                .stream()
                .map(HolidayResponse::from)
                .collect(Collectors.toList());
    }

    // 일별 일정 조회
    public List<ScheduleResponse> getDailySchedules(Long calendarId, LocalDateTime dateTime) {
        Calendar calendar = findCalendarById(calendarId);
        validateCalendarAccess(calendar);

        return scheduleRepository.findDailySchedules(calendarId, dateTime.toLocalDate())
                .stream()
                .map(ScheduleResponse::from)
                .collect(Collectors.toList());
    }

    // 일정 생성
    @Transactional
    public ScheduleResponse createSchedule(Long calendarId, ScheduleCreateRequest request) {
        request.validate();

        Calendar calendar = findCalendarById(calendarId);
        Member currentMember = memberService.getCurrentMember();

        validateCalendarAccess(calendar);

        Schedule schedule = Schedule.builder()
                .calendar(calendar)
                .creator(currentMember)
                .title(request.title())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .description(request.description())
                .category(request.category())
                .emoji(request.emoji())
                .build();

        Schedule savedSchedule = scheduleRepository.save(schedule);
        return ScheduleResponse.from(savedSchedule);
    }

    // 일정 수정
    @Transactional
    public ScheduleResponse updateSchedule(Long calendarId, Long scheduleId, ScheduleCreateRequest request) {
        request.validate(); // Record의 validate 메서드 호출

        Calendar calendar = findCalendarById(calendarId);
        Schedule schedule = findScheduleById(scheduleId);

        validateCalendarAccess(calendar);
        validateScheduleAccess(schedule);
        validateScheduleForCalendar(schedule, calendarId);

        schedule.update(
                request.title(),
                request.startTime(),
                request.endTime(),
                request.description(),
                request.category(),
                request.emoji()
        );

        return ScheduleResponse.from(schedule);
    }

    // 일정 삭제
    @Transactional
    public ScheduleDeleteResponse deleteSchedule(Long calendarId, Long scheduleId) {
        Calendar calendar = findCalendarById(calendarId);
        Schedule schedule = findScheduleById(scheduleId);

        validateCalendarAccess(calendar);
        validateScheduleAccess(schedule);

        String scheduleTitle = schedule.getTitle();

        scheduleRepository.delete(schedule);

        return ScheduleDeleteResponse.of(
                scheduleId,
                calendarId,
                scheduleTitle,
                LocalDateTime.now()
        );
    }

    private Calendar findCalendarById(Long calendarId) {
        return calendarRepository.findById(calendarId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CALENDAR_NOT_FOUND));
    }

    private Schedule findScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SCHEDULE_NOT_FOUND));
    }

    public Schedule findScheduleById(Long calendarId, Long scheduleId) {
        Calendar calendar = findCalendarById(calendarId);
        Schedule schedule = findScheduleById(scheduleId);

        validateCalendarAccess(calendar);

        if (!schedule.getCalendar().getId().equals(calendarId)) {
            throw new BusinessException(ErrorCode.SCHEDULE_NOT_FOUND);
        }

        return schedule;
    }

    private void validateCalendarAccess(Calendar calendar) {
        Member currentMember = memberService.getCurrentMember();
        Couple couple = calendar.getCouple();

        if (!currentMember.getCouple().equals(couple)) {
            throw new BusinessException(ErrorCode.CALENDAR_ACCESS_DENIED);
        }
    }

    private void validateScheduleAccess(Schedule schedule) {
        Member currentMember = memberService.getCurrentMember();

        if (!schedule.getCreator().equals(currentMember)) {
            throw new BusinessException(ErrorCode.SCHEDULE_ACCESS_DENIED);
        }
    }

    private void validateScheduleForCalendar(Schedule schedule, Long calendarId) {
        if (!schedule.getCalendar().getId().equals(calendarId)) {
            throw new BusinessException(ErrorCode.SCHEDULE_NOT_FOUND);
        }
    }

}