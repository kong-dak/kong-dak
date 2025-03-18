package com.kongdak.domain.diary.dto.response;

import java.util.List;

public record DiaryPhotoUploadResponse(
        List<String> photoUrls
) {
}
