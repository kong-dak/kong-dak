package com.kongdak.domain.bucketlist.dto.response;

import com.kongdak.domain.bucketlist.entity.BucketList;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BucketListResponseDto(
        Long bucketId,
        String title,
        String category,
        boolean isCompleted,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        int orderNum
) {
    public static BucketListResponseDto from(BucketList bucketList) {
        return BucketListResponseDto.builder()
                .bucketId(bucketList.getId())
                .title(bucketList.getTitle())
                .category(String.valueOf(bucketList.getCategory()))
                .isCompleted(bucketList.isCompleted())
                .createdAt(bucketList.getCreatedAt())
                .updatedAt(bucketList.getUpdatedAt())
                .orderNum(bucketList.getOrderNum())
                .build();
    }
}
