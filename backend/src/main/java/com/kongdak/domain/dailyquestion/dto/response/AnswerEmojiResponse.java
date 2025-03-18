package com.kongdak.domain.dailyquestion.dto.response;

import com.kongdak.domain.dailyquestion.entity.AnswerEmoji;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "답변 이모지 추가 응답")
public record AnswerEmojiResponse(
        @Schema(description = "이모지 ID", example = "1")
        Long emojiId,

        @Schema(description = "답변 ID", example = "1")
        Long answerId,

        @Schema(description = "이모지를 추가한 사용자 ID", example = "2")
        Long memberId,

        @Schema(description = "추가된 이모지", example = "👍")
        String emoji,

        @Schema(description = "이모지 추가 시간", example = "2024-01-19T18:30:00")
        LocalDateTime createdAt
) {
    public static AnswerEmojiResponse from(AnswerEmoji answerEmoji) {
        return new AnswerEmojiResponse(
                answerEmoji.getId(),
                answerEmoji.getAnswer().getId(),
                answerEmoji.getMember().getId(),
                answerEmoji.getEmoji(),
                answerEmoji.getCreatedAt()
        );
    }
}
