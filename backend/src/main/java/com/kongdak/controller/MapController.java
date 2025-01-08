package com.kongdak.controller;

import com.kongdak.controller.dto.response.KakaoLocalSearchResponse;
import com.kongdak.domain.map.MapService;
import com.kongdak.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/maps")
public class MapController {

    private final MapService mapService;

    @GetMapping("/search")
    public ApiResponse<KakaoLocalSearchResponse> search(
            @RequestParam String query,
            @RequestParam(required = false) String x,
            @RequestParam(required = false) String y,
            @RequestParam(required = false) String radius,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String sort
    ) {
        KakaoLocalSearchResponse response = mapService.search(query, x, y, radius, size, sort);
        return ApiResponse.ok(response);
    }

}
