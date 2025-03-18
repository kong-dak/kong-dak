package com.kongdak.domain.couple.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "커플 연결 해제 응답")
public record CoupleDisconnectResponse(
        @Schema(description = "커플 ID", example = "1")
        Long coupleId,

        @Schema(description = "연결 해제 요청자 ID", example = "2")
        Long requesterId,

        @Schema(description = "연결 해제 시간", example = "2024-01-19T18:30:00")
        LocalDateTime disconnectedAt,

        @Schema(description = "연결 지속 기간(일)", example = "100")
        Long durationDays
) {
    public static CoupleDisconnectResponse of(Long coupleId, Long requesterId, LocalDateTime disconnectedAt, Long durationDays) {
        return new CoupleDisconnectResponse(coupleId, requesterId, disconnectedAt, durationDays);
    }
}