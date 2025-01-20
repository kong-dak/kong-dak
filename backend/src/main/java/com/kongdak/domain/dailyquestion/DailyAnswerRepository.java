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
    @Query("SELECT da FROM DailyAnswer da " +
            "WHERE da.member.id = :memberId " +
            "ORDER BY da.question.id DESC")
    Optional<DailyAnswer> findLastAnswerByMemberId(@Param("memberId") Long memberId);

    // 특정 커플(두 멤버)의 답변 여부 확인
    @Query("SELECT COUNT(da) FROM DailyAnswer da " +
            "WHERE da.question.id = :questionId " +
            "AND da.member.id IN (:member1Id, :member2Id)")
    long countAnswersByQuestionAndMembers(
            @Param("questionId") Long questionId,
            @Param("member1Id") Long member1Id,
            @Param("member2Id") Long member2Id
    );
}
