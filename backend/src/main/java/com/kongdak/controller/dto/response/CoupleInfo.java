package com.kongdak.controller.dto.response;

import com.kongdak.domain.couple.Couple;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "커플 정보")
public record CoupleInfo(
        @Schema(description = "커플 ID", example = "1")
        Long coupleId,

        @Schema(description = "파트너 ID", example = "2")
        Long partnerId,

        @Schema(description = "연결 시간", example = "2024-01-10T12:00:00")
        LocalDateTime connectedAt,

        @Schema(description = "기념일", example = "2024-01-10T00:00:00")
        LocalDateTime anniversaryDate,

        @Schema(description = "연결 상태", example = "true")
        boolean isConnected
) {
    public static CoupleInfo from(Couple couple, Long partnerId) {
        return new CoupleInfo(
                couple.getId(),
                partnerId,
                couple.getConnectedAt(),
                couple.getAnniversaryDate(),
                couple.isConnected()
        );
    }
}
