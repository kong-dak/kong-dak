package com.kongdak.controller.dto.response;

import com.kongdak.domain.dailyquestion.AnswerReply;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "답변 댓글 추가 응답")
public record ReplyCreateResponse(
        @Schema(description = "생성된 댓글 ID", example = "1")
        Long replyId,

        @Schema(description = "질문 ID", example = "1")
        Long questionId,

        @Schema(description = "작성자 ID", example = "1")
        Long memberId,

        @Schema(description = "댓글 내용", example = "정말 좋은 생각이에요!")
        String content,

        @Schema(description = "생성 시간", example = "2024-01-19T18:30:00")
        LocalDateTime createdAt
) {
    public static ReplyCreateResponse from(AnswerReply reply) {
        return new ReplyCreateResponse(
                reply.getId(),
                reply.getQuestion().getId(),
                reply.getMember().getId(),
                reply.getContent(),
                reply.getCreatedAt()
        );
    }
}