package com.kongdak.domain.dailyquestion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DailyQuestionRepository extends JpaRepository<DailyQuestion, Long> {
    // 첫 번째 질문 조회
    Optional<DailyQuestion> findFirstByOrderByIdAsc();

    // 특정 ID 이후의 다음 질문 조회
    @Query("SELECT dq FROM DailyQuestion dq " +
            "WHERE dq.id > :questionId " +
            "ORDER BY dq.id ASC")
    Optional<DailyQuestion> findNextQuestion(@Param("questionId") Long questionId);


    Optional<DailyQuestion> findFirstByIdGreaterThanOrderByIdAsc(Long questionId);

    List<DailyQuestion> findByIdLessThanEqualOrderByIdDesc(Long todayQuestionId);
}






