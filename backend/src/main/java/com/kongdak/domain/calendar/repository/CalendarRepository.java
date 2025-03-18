package com.kongdak.domain.calendar.repository;

import com.kongdak.domain.calendar.entity.Calendar;
import com.kongdak.domain.couple.entity.Couple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    // 커플의 캘린더 조회
    Optional<Calendar> findByCouple(Couple couple);

    // 커플 ID로 캘린더 조회
    Optional<Calendar> findByCoupleId(Long coupleId);

    // 캘린더 존재 여부 확인
    boolean existsByCouple(Couple couple);
}
