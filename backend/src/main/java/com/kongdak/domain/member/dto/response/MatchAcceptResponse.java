package com.kongdak.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "커플 매칭 수락 응답")
public record MatchAcceptResponse(
        @Schema(description = "요청자 ID", example = "1")
        Long requesterId,

        @Schema(description = "수락자 ID", example = "2")
        Long accepterId,

        @Schema(description = "매칭 성사 시간", example = "2024-01-19T18:30:00")
        LocalDateTime matchedAt
) {
    public static MatchAcceptResponse of(Long requesterId, Long accepterId, LocalDateTime matchedAt) {
        return new MatchAcceptResponse(requesterId, accepterId, matchedAt);
    }
}


