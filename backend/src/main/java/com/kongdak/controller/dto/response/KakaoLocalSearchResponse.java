package com.kongdak.controller.dto.response;

import java.util.List;

public record KakaoLocalSearchResponse (
        Meta meta,
        List<Document> documents
) {
    public record Meta(
            int total_count,
            int pageable_count,
            boolean is_end
    ) {}

    public record Document(
            String place_name,
            String address_name,
            String category_name,
            String road_address_name,
            String phone,
            String x,
            String y,
            String place_url,
            String distance
    ) {}
}