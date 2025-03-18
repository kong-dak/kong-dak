package com.kongdak.domain.diary.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "날씨")
public enum Weather {
    @Schema(description = "맑음", example = "SUNNY")
    SUNNY,

    @Schema(description = "흐림", example = "CLOUDY")
    CLOUDY,

    @Schema(description = "비", example = "RAINY")
    RAINY,

    @Schema(description = "눈", example = "SNOWY")
    SNOWY
}

