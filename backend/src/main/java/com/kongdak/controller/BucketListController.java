package com.kongdak.controller;

import com.kongdak.domain.bucketlist.dto.request.BucketListCreateDto;
import com.kongdak.domain.bucketlist.dto.request.BucketListReorderRequestDto;
import com.kongdak.domain.bucketlist.dto.request.BucketListUpdateDto;
import com.kongdak.domain.bucketlist.dto.response.BucketListDeleteResponseDto;
import com.kongdak.domain.bucketlist.dto.response.BucketListReorderResponseDto;
import com.kongdak.domain.bucketlist.dto.response.BucketListResponseDto;
import com.kongdak.domain.bucketlist.service.BucketListService;
import com.kongdak.global.response.BaseResponse;
import com.kongdak.global.security.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bucketlists")
@Tag(name = "버킷리스트", description = "버킷리스트 관련 API")
public class BucketListController {

    private final BucketListService bucketListService;

    @Operation(summary = "버킷리스트 조회", description = "사용자의 모든 버킷리스트를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "버킷리스트 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = BaseResponse.class,
                                    subTypes = {BucketListResponseDto.class}
                            )
                    )
            )
    })
    @GetMapping
    public BaseResponse<List<BucketListResponseDto>> getBucketLists(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<BucketListResponseDto> bucketLists = bucketListService.getBucketLists(userDetails.getId());
        return BaseResponse.ok(bucketLists);
    }

    @Operation(summary = "버킷리스트 생성", description = "새로운 버킷리스트를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "버킷리스트 생성 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = BaseResponse.class,
                                    subTypes = {BucketListResponseDto.class}
                            )
                    )
            )
    })
    @PostMapping
    public BaseResponse<BucketListResponseDto> createBucketList(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody BucketListCreateDto dto
    ) {
        BucketListResponseDto bucketList = bucketListService.createBucketList(userDetails.getId(), dto);
        return BaseResponse.created(bucketList);
    }

    @Operation(summary = "버킷리스트 수정", description = "기존 버킷리스트를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "버킷리스트 수정 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = BaseResponse.class,
                                    subTypes = {BucketListResponseDto.class}
                            )
                    )
            )
    })
    @PatchMapping("/{bucketlistId}")
    public BaseResponse<BucketListResponseDto> updateBucketList(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("bucketlistId") Long bucketListId,
            @RequestBody BucketListUpdateDto dto
    ) {
        BucketListResponseDto bucketListResponseDto = bucketListService.updateBucketList(userDetails.getId(), bucketListId, dto);
        return BaseResponse.ok(bucketListResponseDto);
    }

    @Operation(summary = "버킷리스트 삭제", description = "기존 버킷리스트를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "버킷리스트 삭제 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = BaseResponse.class,
                                    subTypes = {BucketListDeleteResponseDto.class}
                            )
                    )
            )
    })
    @DeleteMapping("/{bucketlistId}")
    public BaseResponse<BucketListDeleteResponseDto> deleteBucketList(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("bucketlistId") Long bucketListId
    ) {
        bucketListService.deleteBucketList(userDetails.getId(), bucketListId);
        return BaseResponse.ok(new BucketListDeleteResponseDto(bucketListId));
    }

    @Operation(summary = "버킷리스트 순서 변경", description = "버킷리스트의 순서를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "버킷리스트 순서 변경 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = BaseResponse.class,
                                    subTypes = {BucketListReorderResponseDto.class}
                            )
                    )
            )
    })
    @PatchMapping("/reorder")
    public BaseResponse<BucketListReorderResponseDto> reorderBucketLists(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody BucketListReorderRequestDto dto) {
        bucketListService.reorderBucketLists(userDetails.getId(), dto.bucketIds());
        return BaseResponse.ok(new BucketListReorderResponseDto(dto.bucketIds()));
    }
}