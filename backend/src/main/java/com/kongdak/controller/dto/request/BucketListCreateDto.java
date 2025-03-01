package com.kongdak.controller.dto.request;

import com.kongdak.domain.bucketlist.BucketListCategory;

public record BucketListCreateDto(
        String title,
        BucketListCategory category
) {
}
