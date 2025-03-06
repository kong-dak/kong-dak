package com.kongdak.controller.dto.response;

import java.util.List;

public record BucketListReorderResponseDto(
        List<Long>bucketIds
) {
}
