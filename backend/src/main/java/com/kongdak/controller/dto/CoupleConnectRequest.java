package com.kongdak.controller.dto;

import java.time.LocalDateTime;

public record CoupleConnectRequest(Long partnerId, LocalDateTime anniversaryDate) {
}
