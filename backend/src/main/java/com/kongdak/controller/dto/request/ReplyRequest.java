package com.kongdak.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "댓글 작성 요청")
public record ReplyRequest(
        @Schema(description = "댓글 내용", example = "정말 좋았겠네요!")
        @NotBlank(message = "댓글 내용은 필수입니다")
        String content
) {}
