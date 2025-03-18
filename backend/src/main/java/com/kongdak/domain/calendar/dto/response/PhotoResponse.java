package com.kongdak.domain.calendar.dto.response;

import com.kongdak.domain.diary.entity.DiaryPhoto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사진 응답")
public record PhotoResponse(
        @Schema(description = "사진 ID", example = "1")
        Long photoId,

        @Schema(description = "DB에 저장된 URL", example = "diary/origin_photo.jpg")
        String dbPhotoUrl,
        @Schema(description = "S3에서 제공되는 presigned URL", example = "https://example.com/photo.jpg")
        String S3PhotoUrl,

        @Schema(description = "S3에서 제공되는 presigned 썸네일 URL", example = "https://example.com/thumbnail.jpg")
        String thumbnailUrl
) {
}
