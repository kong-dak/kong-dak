package com.kongdak.controller.dto.response;

import com.kongdak.domain.dailyquestion.AnswerReply;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "답변 댓글 삭제 응답")
public record ReplyDeleteResponse(
        @Schema(description = "삭제된 댓글 ID", example = "1")
        Long replyId,

        @Schema(description = "질문 ID", example = "1")
        Long questionId,

        @Schema(description = "작성자 ID", example = "1")
        Long memberId,

        @Schema(description = "삭제 시간", example = "2024-01-19T18:30:00")
        LocalDateTime deletedAt
) {
    public static ReplyDeleteResponse of(AnswerReply reply) {
        return new ReplyDeleteResponse(
                reply.getId(),
                reply.getQuestion().getId(),
                reply.getMember().getId(),
                LocalDateTime.now()
        );
    }
}