package com.kongdak.domain.bucketlist.dto.request;

import com.kongdak.domain.bucketlist.entity.BucketListCategory;

public record BucketListCreateDto(
        String title,
        BucketListCategory category
) {
}
