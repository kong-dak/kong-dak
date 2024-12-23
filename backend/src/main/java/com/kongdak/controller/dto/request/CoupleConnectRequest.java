package com.kongdak.controller.dto.request;

import java.time.LocalDateTime;

public record CoupleConnectRequest(Long partnerId, LocalDateTime anniversaryDate) {
}
