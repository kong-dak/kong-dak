package com.kongdak.controller.dto.response;

import com.kongdak.domain.diary.DiaryPhoto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사진 응답")
public record PhotoResponse(
        @Schema(description = "사진 ID", example = "1")
        Long photoId,

        @Schema(description = "원본 URL", example = "https://example.com/photo.jpg")
        String photoUrl,

        @Schema(description = "썸네일 URL", example = "https://example.com/thumbnail.jpg")
        String thumbnailUrl
) {
    public static PhotoResponse from(DiaryPhoto photo) {
        return new PhotoResponse(
                photo.getId(),
                photo.getPhotoUrl(),
                photo.getThumbnailUrl()
        );
    }
}
