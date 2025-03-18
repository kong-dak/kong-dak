package com.kongdak.domain.diary.dto.response;

import java.util.List;
import java.util.Map;

public record DiaryPhotoUploadResponse(
        List<String> photoUrls,
        Map<String, String> previewUrls
) {
}
