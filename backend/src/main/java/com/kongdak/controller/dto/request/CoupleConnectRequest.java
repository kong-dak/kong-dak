package com.kongdak.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "커플 연결 요청")
public record CoupleConnectRequest(
        @Schema(description = "상대방 ID", example = "2", required = true)
        Long partnerId,

        @Schema(description = "기념일", example = "2024-01-10T00:00:00", required = true)
        LocalDateTime anniversaryDate
) {}
