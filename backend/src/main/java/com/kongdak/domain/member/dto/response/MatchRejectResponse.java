package com.kongdak.domain.member.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "커플 매칭 거절 응답")
public record MatchRejectResponse(
        @Schema(description = "요청자 ID", example = "1")
        Long requesterId,

        @Schema(description = "거절자 ID", example = "2")
        Long rejecterId,

        @Schema(description = "거절 시간", example = "2024-01-19T18:30:00")
        LocalDateTime rejectedAt
) {
    public static MatchRejectResponse of(Long requesterId, Long rejecterId, LocalDateTime rejectedAt) {
        return new MatchRejectResponse(requesterId, rejecterId, rejectedAt);
    }
}
