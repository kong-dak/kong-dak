package com.kongdak.controller.dto.response;

import com.kongdak.domain.dailyquestion.AnswerReply;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Schema(description = "답변 댓글 응답")
@Builder
public record AnswerReplyResponse(
        @Schema(description = "댓글 ID", example = "1")
        Long replyId,

        @Schema(description = "댓글 작성자 ID", example = "1")
        Long memberId,

        @Schema(description = "댓글 내용", example = "좋은 생각이네요!")
        String content,

        @Schema(description = "댓글 작성 시간", example = "2024-01-19T18:30:00")
        LocalDateTime createdAt
) {
    public static AnswerReplyResponse from(AnswerReply reply) {
        return AnswerReplyResponse.builder()
                .replyId(reply.getId())
                .memberId(reply.getMember().getId())
                .content(reply.getContent())
                .createdAt(reply.getCreatedAt())
                .build();
    }
}