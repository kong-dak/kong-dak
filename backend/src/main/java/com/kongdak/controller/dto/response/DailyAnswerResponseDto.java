package com.kongdak.controller.dto.response;

import com.kongdak.domain.dailyquestion.DailyAnswer;

import java.time.LocalDateTime;

public record DailyAnswerResponseDto(
        Long answerId,
        Long memberId,
        String content,
        LocalDateTime createdAt
) {
    public static DailyAnswerResponseDto from(DailyAnswer answer) {
        return new DailyAnswerResponseDto(
                answer.getId(),
                answer.getMember().getId(),
                answer.getContent(),
                answer.getCreatedAt()
        );
    }
}
