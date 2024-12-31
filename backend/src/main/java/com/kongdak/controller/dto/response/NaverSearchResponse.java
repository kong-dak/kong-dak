package com.kongdak.controller.dto.response;

public record NaverSearchResponse(
        String title,
        String link,
        String category,
        String description,
        String telephone,
        String address,
        String roadAddress,
        String mapx,
        String mapy
) {}
