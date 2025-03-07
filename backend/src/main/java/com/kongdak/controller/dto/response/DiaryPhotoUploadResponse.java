package com.kongdak.controller.dto.response;

import java.util.List;

public record DiaryPhotoUploadResponse(
        List<String> photoUrls
) {
}
