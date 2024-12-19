package com.kongdak.controller;

import com.kongdak.controller.dto.MemberResponse;
import com.kongdak.controller.dto.UpdateNicknameRequest;
import com.kongdak.domain.member.Member;
import com.kongdak.domain.member.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<MemberResponse> getMemberInfo(@AuthenticationPrincipal UserDetails userDetails) {
        Member member = memberService.findMemberById(Long.parseLong(userDetails.getUsername()));
        return ResponseEntity.ok(MemberResponse.from(member));
    }

    @PatchMapping("/nickname")
    public ResponseEntity<MemberResponse> updateNickname(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid UpdateNicknameRequest request) {

        Member member = memberService.updateNickname(
                Long.parseLong(userDetails.getUsername()),
                request.nickname()
        );
        return ResponseEntity.ok(MemberResponse.from(member));
    }

    @DeleteMapping
    public ResponseEntity<Void> deactivateMember(@AuthenticationPrincipal UserDetails userDetails) {
        memberService.deactivateMember(Long.parseLong(userDetails.getUsername()));
        return ResponseEntity.ok().build();
    }
}