package com.kongdak.domain.map.dto.response;

import com.kongdak.domain.map.PlaceOperatingHour;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "장소 운영 시간")
public record PlaceOperatingHourResponse(
        @Schema(description = "요일") String dayOfWeek,
        @Schema(description = "오픈 시간") String openTime,
        @Schema(description = "마감 시간") String closeTime
) {
    public static PlaceOperatingHourResponse from(PlaceOperatingHour operatingHour) {
        return new PlaceOperatingHourResponse(
                operatingHour.getDayOfWeek(),
                operatingHour.getOpenTime(),
                operatingHour.getCloseTime()
        );
    }
}
