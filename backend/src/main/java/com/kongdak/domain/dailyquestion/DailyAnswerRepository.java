package com.kongdak.domain.dailyquestion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DailyAnswerRepository extends JpaRepository<DailyAnswer, Long> {
    // 특정 질문에 대한 답변 조회
    List<DailyAnswer> findByQuestionId(Long questionId);
    // 특정 멤버의 답변 조회
    Optional<DailyAnswer> findByQuestionIdAndMemberId(Long questionId, Long memberId);

    // 특정 멤버의 마지막 답변 조회
    Optional<DailyAnswer> findFirstByMemberIdOrderByQuestionIdDesc(Long memberId);


    // 답변 수 세기
    @Query("SELECT COUNT(da) FROM DailyAnswer da " +
            "WHERE da.question.id = :questionId " +
            "AND da.member.couple.id = :coupleId")
    long countByQuestionIdAndCoupleId(@Param("questionId") Long questionId, @Param("coupleId") Long coupleId);

    // 이모지 Fetch Join 하여 가져오기
    @Query("SELECT DISTINCT a FROM DailyAnswer a " +
            "LEFT JOIN FETCH a.emojis " +
            "WHERE a.question.id = :questionId")
    List<DailyAnswer> findByQuestionIdWithEmojis(@Param("questionId") Long questionId);
}
