package com.kongdak.controller.dto.response;

import com.kongdak.domain.diary.DiaryPhoto;

public record PhotoResponse(
        Long photoId,
        String photoUrl,
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
