package com.kongdak.domain.dailyquestion.dto.response;

import com.kongdak.domain.dailyquestion.entity.AnswerReply;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "답글 응답")
public record ReplyResponse(
        @Schema(description = "답글 ID", example = "1")
        Long replyId,

        @Schema(description = "작성자 ID", example = "1")
        Long memberId,

        @Schema(description = "답글 내용", example = "좋은 생각이네요!")
        String content,

        @Schema(description = "작성 시간", example = "2024-01-10T12:00:00")
        LocalDateTime createdAt
) {
    public static ReplyResponse from(AnswerReply reply) {
        return new ReplyResponse(
                reply.getId(),
                reply.getMember().getId(),
                reply.getContent(),
                reply.getCreatedAt()
        );
    }
}
