package com.kongdak.domain.dailyquestion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface DailyQuestionRepository extends JpaRepository<DailyQuestion, Long> {
    // 오늘의 질문 조회
    Optional<DailyQuestion> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}






