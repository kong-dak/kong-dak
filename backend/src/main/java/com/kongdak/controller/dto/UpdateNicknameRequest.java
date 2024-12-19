package com.kongdak.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateNicknameRequest(
        @NotBlank(message = "Nickname cannot be blanck")
        @Size(min = 2, max = 20, message = "Nickname must be between 2 and 20 characters")
        String nickname
) {

}
