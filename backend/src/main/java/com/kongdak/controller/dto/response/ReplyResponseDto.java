package com.kongdak.controller.dto.response;

import com.kongdak.domain.dailyquestion.AnswerReply;

import java.time.LocalDateTime;

public record ReplyResponseDto(
        Long replyId,
        Long memberId,
        String content,
        LocalDateTime createdAt
) {
    public static ReplyResponseDto from(AnswerReply reply) {
        return new ReplyResponseDto(
                reply.getId(),
                reply.getMember().getId(),
                reply.getContent(),
                reply.getCreatedAt()
        );
    }
}
