package com.kongdak.domain.calendar;

import com.kongdak.controller.dto.ScheduleCreateRequest;
import com.kongdak.domain.couple.Couple;
import com.kongdak.domain.member.Member;
import com.kongdak.domain.member.MemberService;
import com.kongdak.global.exception.BusinessException;
import com.kongdak.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    public Calendar createCalendar(Couple couple) {
        if (calendarRepository.existsByCouple(couple)) {
            throw new BusinessException(ErrorCode.DUPLICATE_CALENDAR);
        }

        Calendar calendar = Calendar.builder()
                .couple(couple)
                .build();

        return calendarRepository.save(calendar);
    }

    // 월별 일정 조회
    public List<Schedule> getMonthlySchedules(Long calendarId, LocalDateTime dateTime) {
        Calendar calendar = findCalendarById(calendarId);

        validateCalendarAccess(calendar);

        int year = dateTime.getYear();
        int month = dateTime.getMonthValue();

        List<Schedule> schedules = scheduleRepository.findMonthlySchedules(calendarId, year, month);
        List<Holiday> holidays = holidayRepository.findByYearAndMonth(year, month);

        schedules.addAll(convertHolidaysToSchedules(calendar, holidays));

        return schedules;
    }

    // 일별 일정 조회
    public List<Schedule> getDailySchedules(Long calendarId, LocalDateTime dateTime) {
        Calendar calendar = findCalendarById(calendarId);

        validateCalendarAccess(calendar);

        return scheduleRepository.findDailySchedules(calendarId, dateTime.toLocalDate());
    }

    // 일정 생성
    @Transactional
    public Schedule createSchedule(Long calendarId, ScheduleCreateRequest request) {
        request.validate(); // Record의 validate 메서드 호출

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

        return scheduleRepository.save(schedule);
    }

    // 일정 수정
    @Transactional
    public Schedule updateSchedule(Long calendarId, Long scheduleId, ScheduleCreateRequest request) {
        request.validate(); // Record의 validate 메서드 호출

        Calendar calendar = findCalendarById(calendarId);
        Schedule schedule = findScheduleById(scheduleId);

        validateCalendarAccess(calendar);
        validateScheduleAccess(schedule);

        schedule.update(
                request.title(),
                request.startTime(),
                request.endTime(),
                request.description(),
                request.category(),
                request.emoji()
        );

        return schedule;
    }

    // 일정 삭제
    @Transactional
    public void deleteSchedule(Long calendarId, Long scheduleId) {
        Calendar calendar = findCalendarById(calendarId);
        Schedule schedule = findScheduleById(scheduleId);

        validateCalendarAccess(calendar);
        validateScheduleAccess(schedule);

        scheduleRepository.delete(schedule);
    }

    private Calendar findCalendarById(Long calendarId) {
        return calendarRepository.findById(calendarId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CALENDAR_NOT_FOUND));
    }

    private Schedule findScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SCHEDULE_NOT_FOUND));
    }

    private void validateCalendarAccess(Calendar calendar) {
        Member currentMember = memberService.getCurrentMember();
        Couple couple = calendar.getCouple();

        if (!couple.getMember1().equals(currentMember) && !couple.getMember2().equals(currentMember)) {
            throw new BusinessException(ErrorCode.CALENDAR_ACCESS_DENIED);
        }
    }

    private void validateScheduleAccess(Schedule schedule) {
        Member currentMember = memberService.getCurrentMember();

        if (!schedule.getCreator().equals(currentMember)) {
            throw new BusinessException(ErrorCode.SCHEDULE_ACCESS_DENIED);
        }
    }

    private List<Schedule> convertHolidaysToSchedules(Calendar calendar, List<Holiday> holidays) {
        return holidays.stream()
                .map(holiday -> Schedule.builder()
                        .calendar(calendar)
                        .title(holiday.getName())
                        .startTime(holiday.getDate().atStartOfDay())
                        .endTime(holiday.getDate().atTime(LocalTime.MAX))
                        .description(holiday.getDescription())
                        .category(ScheduleCategory.SHARED)
                        .isHoliday(true)
                        .build())
                .collect(Collectors.toList());
    }
}