package com.kongdak.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "닉네임 수정 요청")
public record NicknameUpdateRequest(
        @Schema(description = "변경할 닉네임", example = "새로운닉네임")
        @NotBlank(message = "닉네임은 필수입니다")
        @Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하여야 합니다")
        String nickname
) {

}
