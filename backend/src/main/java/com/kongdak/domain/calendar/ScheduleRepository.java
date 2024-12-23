package com.kongdak.domain.calendar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    // 특정 캘린더의 월간 일정 조회 (시작 시간 기준)
    @Query("SELECT s FROM Schedule s WHERE s.calendar.id = :calendarId " +
            "AND FUNCTION('YEAR', s.startTime) = :year " +
            "AND FUNCTION('MONTH', s.startTime) = :month " +
            "ORDER BY s.startTime ASC")
    List<Schedule> findMonthlySchedules(
            @Param("calendarId") Long calendarId,
            @Param("year") int year,
            @Param("month") int month
    );

    // 특정 캘린더의 일간 일정 조회
    @Query("SELECT s FROM Schedule s WHERE s.calendar.id = :calendarId " +
            "AND DATE(s.startTime) = :date " +
            "ORDER BY s.startTime ASC")
    List<Schedule> findDailySchedules(
            @Param("calendarId") Long calendarId,
            @Param("date") LocalDate date
    );

    // 특정 기간 내 일정 조회
    List<Schedule> findByCalendarIdAndStartTimeBetweenOrderByStartTimeAsc(
            Long calendarId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    // 동일 시간대 일정 존재 여부 확인 (일정 중복 체크)
    @Query("SELECT COUNT(s) > 0 FROM Schedule s WHERE s.calendar.id = :calendarId " +
            "AND s.id != :excludeId " +
            "AND ((s.startTime BETWEEN :startTime AND :endTime) " +
            "OR (s.endTime BETWEEN :startTime AND :endTime) " +
            "OR (:startTime BETWEEN s.startTime AND s.endTime))")
    boolean existsOverlappingSchedule(
            @Param("calendarId") Long calendarId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("excludeId") Long excludeId
    );

    // 특정 멤버가 작성한 일정 조회
    List<Schedule> findByCreatorIdOrderByStartTimeDesc(Long memberId);

    // 특정 캘린더의 모든 일정 삭제
    void deleteByCalendarId(Long calendarId);
}
