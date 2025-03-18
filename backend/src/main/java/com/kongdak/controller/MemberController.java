package com.kongdak.controller;

import com.kongdak.domain.member.dto.request.NicknameUpdateRequest;
import com.kongdak.domain.member.dto.response.ActivateResponse;
import com.kongdak.domain.member.dto.response.DeactivateResponse;
import com.kongdak.domain.member.dto.response.MemberNicknameChangeResponse;
import com.kongdak.domain.member.dto.response.MemberResponse;
import com.kongdak.domain.member.service.MemberService;
import com.kongdak.global.response.BaseResponse;
import com.kongdak.global.security.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Tag(name = "회원", description = "회원 관련 API")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원 정보 조회", description = "현재 로그인한 회원의 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @GetMapping
    public BaseResponse<MemberResponse> getMemberInfo(
            @Parameter(description = "인증된 사용자 정보", hidden = true)
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return BaseResponse.ok(memberService.getMemberInfo(userDetails.getEmail()));
    }

    @Operation(summary = "닉네임 수정", description = "회원의 닉네임을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "수정 성공",
            content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @PatchMapping("/nickname")
    public BaseResponse<MemberNicknameChangeResponse> updateNickname(
            @Parameter(description = "인증된 사용자 정보", hidden = true)
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Parameter(description = "변경할 닉네임 정보")
            @RequestBody @Valid NicknameUpdateRequest request) {
        return BaseResponse.ok(memberService.updateNickname(
                userDetails.getUsername(),
                request.nickname()
        ));
    }

    @Operation(summary = "회원 탈퇴", description = "회원 계정을 비활성화합니다.")
    @ApiResponse(responseCode = "200", description = "탈퇴 성공",
            content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @PatchMapping("/deactivate")
    public BaseResponse<DeactivateResponse> deactivateMember(
            @Parameter(description = "인증된 사용자 정보", hidden = true)
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        return BaseResponse.ok(memberService.deactivateMember(userDetails.getUsername()));
    }

    @Operation(summary = "회원 복구", description = "회원 계정을 활성화합니다.")
    @ApiResponse(responseCode = "200", description = "복구 성공",
            content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @PatchMapping("/activate")
    public BaseResponse<ActivateResponse> activateMember(
            @Parameter(description = "인증된 사용자 정보", hidden = true)
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        return BaseResponse.ok(memberService.activateMember(userDetails.getUsername()));
    }
}