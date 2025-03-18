package com.kongdak.domain.dailyquestion.repository;

import com.kongdak.domain.dailyquestion.entity.AnswerReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerReplyRepository extends JpaRepository<AnswerReply, Long> {
    // 질문에 달린 전체 댓글 수 조회
    int countByQuestionId(Long questionId);

    // 특정 질문의 모든 댓글 조회
    List<AnswerReply> findByQuestionId(Long questionId);

    List<AnswerReply> findByQuestionIdOrderByCreatedAtDesc(Long questionId);
}
