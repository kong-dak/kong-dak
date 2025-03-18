package com.kongdak.domain.bucketlist.dto.request;

import java.util.List;

public record BucketListReorderRequestDto(
        List<Long> bucketIds
) {
}
