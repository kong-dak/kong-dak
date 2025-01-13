package com.kongdak.controller;

import com.kongdak.controller.dto.response.KakaoLocalSearchResponse;
import com.kongdak.domain.map.MapService;
import com.kongdak.global.exception.ErrorResponse;
import com.kongdak.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/maps")
@Tag(name = "지도", description = "지도 관련 API")
public class MapController {

    private final MapService mapService;

    @Operation(
            summary = "키워드로 장소 검색",
            description = "카카오 로컬 API를 활용하여 키워드로 장소를 검색합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "검색 성공",
                    content = @Content(schema = @Schema(implementation = KakaoLocalSearchResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping("/search")
    public BaseResponse<KakaoLocalSearchResponse> search(
            @Parameter(description = "검색 키워드", required = true) @RequestParam String query,
            @Parameter(description = "중심 좌표 X (경도)") @RequestParam(required = false) String x,
            @Parameter(description = "중심 좌표 Y (위도)") @RequestParam(required = false) String y,
            @Parameter(description = "검색 반경 (미터 단위)") @RequestParam(required = false) String radius,
            @Parameter(description = "결과 개수") @RequestParam(required = false) String size,
            @Parameter(description = "정렬 기준 (accuracy/distance)") @RequestParam(required = false) String sort
    ) {
        KakaoLocalSearchResponse response = mapService.search(query, x, y, radius, size, sort);
        return BaseResponse.ok(response);
    }
}
