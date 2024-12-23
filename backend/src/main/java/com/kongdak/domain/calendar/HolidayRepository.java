package com.kongdak.domain.calendar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {
    // 특정 연도의 모든 공휴일 조회
    @Query("SELECT h FROM Holiday h WHERE FUNCTION('YEAR', h.date) = :year " +
            "ORDER BY h.date ASC")
    List<Holiday> findAllByYear(@Param("year") int year);

    // 특정 월의 공휴일 조회
    @Query("SELECT h FROM Holiday h WHERE FUNCTION('YEAR', h.date) = :year " +
            "AND FUNCTION('MONTH', h.date) = :month " +
            "ORDER BY h.date ASC")
    List<Holiday> findByYearAndMonth(
            @Param("year") int year,
            @Param("month") int month
    );

    // 특정 기간 내 공휴일 조회
    List<Holiday> findByDateBetweenOrderByDateAsc(
            LocalDate startDate,
            LocalDate endDate
    );

    // 반복되는 공휴일 조회 (매년 반복되는 공휴일)
    @Query("SELECT h FROM Holiday h WHERE h.isRecurringYearly = true " +
            "AND FUNCTION('MONTH', h.date) = :month " +
            "AND FUNCTION('DAY', h.date) = :day")
    List<Holiday> findRecurringHolidays(
            @Param("month") int month,
            @Param("day") int day
    );

    // 특정 날짜가 공휴일인지 확인
    boolean existsByDate(LocalDate date);

    // 이름으로 공휴일 검색
    List<Holiday> findByNameContainingOrderByDateAsc(String name);
}