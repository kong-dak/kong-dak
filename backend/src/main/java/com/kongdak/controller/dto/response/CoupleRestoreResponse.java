package com.kongdak.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "커플 연결 복구 응답")
public record CoupleRestoreResponse(
        @Schema(description = "커플 ID", example = "1")
        Long coupleId,

        @Schema(description = "복구 요청자 ID", example = "2")
        Long requesterId,

        @Schema(description = "연결 복구 시간", example = "2024-01-19T18:30:00")
        LocalDateTime restoredAt,

        @Schema(description = "원래 연결 시작 시간", example = "2023-12-25T00:00:00")
        LocalDateTime originalConnectedAt
) {
    public static CoupleRestoreResponse of(
            Long coupleId,
            Long requesterId,
            LocalDateTime restoredAt,
            LocalDateTime originalConnectedAt) {
        return new CoupleRestoreResponse(coupleId, requesterId, restoredAt, originalConnectedAt);
    }
}