package com.kongdak.domain.bucketlist.dto.response;

import java.util.List;

public record BucketListReorderResponseDto(
        List<Long>bucketIds
) {
}
