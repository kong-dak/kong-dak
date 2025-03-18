package com.kongdak.domain.bucketlist.dto.request;

public record BucketListUpdateDto(
        String title,
        Boolean isCompleted
) {
}
