package com.kongdak.controller.dto.response;

import java.util.List;

public record NaverSearchApiResponse(
        String lastBuildDate,
        int total,
        int start,
        int display,
        List<NaverSearchResponse> items
) {
}
