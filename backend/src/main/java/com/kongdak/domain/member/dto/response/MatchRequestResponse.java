package com.kongdak.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "커플 매칭 요청 응답")
public record MatchRequestResponse(
        @Schema(description = "매칭 요청 ID", example = "123456")
        String requestId,

        @Schema(description = "요청자 ID", example = "1")
        Long requesterId,

        @Schema(description = "대상자 ID", example = "2")
        Long targetId,

        @Schema(description = "요청 시간", example = "2024-01-19T18:30:00")
        LocalDateTime requestedAt
) {}