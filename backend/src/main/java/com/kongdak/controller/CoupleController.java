package com.kongdak.controller;

import com.kongdak.controller.dto.request.CoupleConnectRequest;
import com.kongdak.controller.dto.response.CoupleResponse;
import com.kongdak.domain.couple.CoupleService;
import com.kongdak.global.security.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/couples")
@RequiredArgsConstructor
public class CoupleController {

    private final CoupleService coupleService;

    @PostMapping
    public ResponseEntity<CoupleResponse> connect(@RequestBody CoupleConnectRequest request,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        CoupleResponse response = coupleService.connect(
                Long.parseLong(userDetails.getUsername()),
                request.partnerId(),
                request.anniversaryDate()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{coupleId}/disconnect")
    public ResponseEntity<Void> disconnect(@PathVariable Long coupleId,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        coupleService.disconnect(coupleId, Long.parseLong(userDetails.getUsername()));
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{coupleId}/restore")
    public ResponseEntity<Void> restore(@PathVariable Long coupleId,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        coupleService.restore(coupleId, Long.parseLong(userDetails.getUsername()));
        return ResponseEntity.ok().build();
    }
}
