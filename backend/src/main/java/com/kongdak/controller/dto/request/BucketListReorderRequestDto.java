package com.kongdak.controller.dto.request;

import java.util.List;

public record BucketListReorderRequestDto(
        List<Long> bucketIds
) {
}
