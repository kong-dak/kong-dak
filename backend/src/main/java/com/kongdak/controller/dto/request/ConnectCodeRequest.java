package com.kongdak.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "연결 코드 요청")
public record ConnectCodeRequest(
        @Schema(description = "연결 코드", example = "123456")
        String code
) {
}
