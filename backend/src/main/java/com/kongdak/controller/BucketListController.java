package com.kongdak.controller;

import com.kongdak.controller.dto.request.BucketListCreateDto;
import com.kongdak.controller.dto.request.BucketListReorderRequestDto;
import com.kongdak.controller.dto.request.BucketListUpdateDto;
import com.kongdak.controller.dto.response.BucketListDeleteResponseDto;
import com.kongdak.controller.dto.response.BucketListReorderResponseDto;
import com.kongdak.controller.dto.response.BucketListResponseDto;
import com.kongdak.domain.bucketlist.BucketListService;
import com.kongdak.global.response.BaseResponse;
import com.kongdak.global.security.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BucketListController {

    private final BucketListService bucketListService;

    @GetMapping("/bucketlists")
    public BaseResponse<List<BucketListResponseDto>> getBucketLists(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<BucketListResponseDto> bucketLists = bucketListService.getBucketLists(userDetails.getId());
        return BaseResponse.ok(bucketLists);
    }

    @PostMapping("/buketlists")
    public BaseResponse<BucketListResponseDto> createBucketList(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody BucketListCreateDto dto
    ) {
        BucketListResponseDto bucketList = bucketListService.createBucketList(userDetails.getId(), dto);
        return BaseResponse.created(bucketList);
    }

    @PatchMapping("/bucketlists/{bucketlistId}")
    public BaseResponse<BucketListResponseDto> updateBucketList(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("bucketlistId") Long bucketListId,
            @RequestBody BucketListUpdateDto dto
    ) {
        BucketListResponseDto bucketListResponseDto = bucketListService.updateBucketList(userDetails.getId(), bucketListId, dto);
        return BaseResponse.ok(bucketListResponseDto);
    }

    @DeleteMapping("bucketlists/{bucketlistId}")
    public BaseResponse<BucketListDeleteResponseDto> deleteBucketList(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("bucketlistId") Long bucketListId
    ) {
        bucketListService.deleteBucketList(userDetails.getId(), bucketListId);
        return BaseResponse.ok(new BucketListDeleteResponseDto(bucketListId));
    }

    @PatchMapping("/bucketlists/reorder")
    public BaseResponse<BucketListReorderResponseDto> reorderBucketLists(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody BucketListReorderRequestDto dto) {
        bucketListService.reorderBucketLists(userDetails.getId(), dto.bucketIds());
        return BaseResponse.ok(new BucketListReorderResponseDto(dto.bucketIds()));
    }
}
