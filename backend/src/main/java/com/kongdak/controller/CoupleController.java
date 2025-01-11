package com.kongdak.controller;

import com.kongdak.controller.dto.request.CoupleConnectRequest;
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
