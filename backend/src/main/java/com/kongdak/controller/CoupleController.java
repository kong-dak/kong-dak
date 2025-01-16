package com.kongdak.controller;

import com.kongdak.controller.dto.request.ConnectCodeRequest;
import com.kongdak.controller.dto.request.CoupleConnectRequest;
import com.kongdak.controller.dto.request.CoupleMatchRequest;
import com.kongdak.controller.dto.response.CoupleMatchCodeResponse;
import com.kongdak.controller.dto.response.CoupleResponse;
import com.kongdak.domain.couple.CoupleService;
import com.kongdak.global.exception.ErrorResponse;
import com.kongdak.global.response.BaseResponse;
import com.kongdak.global.security.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/couples")
@RequiredArgsConstructor
@Tag(name = "커플", description = "커플 관련 API")
public class CoupleController {

    private final CoupleService coupleService;

    @GetMapping("/code")
    @Operation(summary = "연결 코드 조회", description = "현재 사용자의 연결 코드를 조회합니다.")
    public BaseResponse<CoupleMatchCodeResponse> getConnectCode(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        String code = coupleService.getConnectCode(Long.parseLong(userDetails.getUsername()));
        return BaseResponse.ok(new CoupleMatchCodeResponse(code));
    }

    @PostMapping("/match")
    @Operation(summary = "커플 매칭 요청", description = "상대방의 연결 코드로 커플 매칭을 요청합니다.")
    public BaseResponse<Void> requestMatch(
            @RequestBody @Valid ConnectCodeRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        coupleService.requestMatch(
                Long.parseLong(userDetails.getUsername()),
                request.code()
        );
        return BaseResponse.ok();
    }

    @PostMapping("/match/{requestId}/accept")
    @Operation(summary = "매칭 수락", description = "커플 매칭 요청을 수락합니다.")
    public BaseResponse<Void> acceptMatch(
            @PathVariable String requestId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        coupleService.acceptMatch(requestId, Long.parseLong(userDetails.getUsername()));
        return BaseResponse.ok();
    }

    @PostMapping("/match/{requestId}/reject")
    @Operation(summary = "매칭 거절", description = "커플 매칭 요청을 거절합니다.")
    public BaseResponse<Void> rejectMatch(
            @PathVariable String requestId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        coupleService.rejectMatch(requestId, Long.parseLong(userDetails.getUsername()));
        return BaseResponse.ok();
    }

    @Operation(
            summary = "커플 연결",
            description = "상대방과 커플 연결을 합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "요청 처리 완료",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping
    public BaseResponse<CoupleResponse> connect(@RequestBody @Valid
                                                @Parameter(description = "커플 연결 요청") CoupleConnectRequest request,
                                                @Parameter(description = "인증된 사용자 정보", hidden = true)
                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        CoupleResponse response = coupleService.connect(
                Long.parseLong(userDetails.getUsername()),
                request.partnerId(),
                request.anniversaryDate()
        );
        return BaseResponse.created(response);
    }

    @Operation(
            summary = "커플 연결 해제",
            description = "커플 연결을 해제합니다. 50일의 유예 기간이 주어집니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "커플 연결 해제 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PatchMapping("/{coupleId}/disconnect")
    public BaseResponse<Void> disconnect(
                                            @Parameter(description = "커플 ID", required = true)
                                            @PathVariable Long coupleId,
                                           @Parameter(description = "인증된 사용자 정보", hidden = true)
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        coupleService.disconnect(coupleId, Long.parseLong(userDetails.getUsername()));
        return BaseResponse.ok();
    }

    @Operation(
            summary = "커플 연결 복구",
            description = "해제된 커플 연결을 복구합니다. 유예 기간 내에만 가능합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "커플 연결 복구 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PatchMapping("/{coupleId}/restore")
    public BaseResponse<Void> restore(@PathVariable Long coupleId,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        coupleService.restore(coupleId, Long.parseLong(userDetails.getUsername()));
        return BaseResponse.ok();
    }
}
