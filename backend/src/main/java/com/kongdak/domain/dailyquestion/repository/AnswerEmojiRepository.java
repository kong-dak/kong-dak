package com.kongdak.domain.dailyquestion.repository;

import com.kongdak.domain.dailyquestion.entity.AnswerEmoji;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerEmojiRepository extends JpaRepository<AnswerEmoji, Long> {
    // 특정 답변의 이모지 조회
    List<AnswerEmoji> findByAnswerId(Long answerId);
}