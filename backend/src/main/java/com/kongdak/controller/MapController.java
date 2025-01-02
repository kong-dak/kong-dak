package com.kongdak.controller;

import com.kongdak.controller.dto.response.NaverSearchResponse;
import com.kongdak.domain.calendar.MapService;
import com.kongdak.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/maps")
public class MapController {

    private final MapService mapService;

    @GetMapping("/search")
    public ApiResponse<List<NaverSearchResponse>> search(
            @RequestParam String query,
            @RequestParam(required = false, defaultValue = "10") int display,
            @RequestParam(required = false, defaultValue = "1") int start
    ) {
        List<NaverSearchResponse> searchData = mapService.search(query, display, start);
        return ApiResponse.ok(searchData);
    }

}
