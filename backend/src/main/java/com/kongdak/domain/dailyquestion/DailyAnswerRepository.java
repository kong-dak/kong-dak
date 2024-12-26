package com.kongdak.domain.dailyquestion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DailyAnswerRepository extends JpaRepository<DailyAnswer, Long> {
    // 특정 질문에 대한 답변 조회
    List<DailyAnswer> findByQuestionId(Long questionId);
    // 특정 멤버의 답변 조회
    Optional<DailyAnswer> findByQuestionIdAndMemberId(Long questionId, Long memberId);
}
