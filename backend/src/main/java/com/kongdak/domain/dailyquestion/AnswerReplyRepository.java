package com.kongdak.domain.dailyquestion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerReplyRepository extends JpaRepository<AnswerReply, Long> {
    // 특정 질문의 모든 댓글 조회
    List<AnswerReply> findByQuestionId(Long questionId);
}
