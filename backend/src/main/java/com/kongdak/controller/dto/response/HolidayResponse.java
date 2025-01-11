package com.kongdak.controller.dto.response;

import com.kongdak.domain.calendar.Holiday;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "공휴일 응답")
public record HolidayResponse(
        @Schema(description = "공휴일 이름", example = "설날")
        String name,

        @Schema(description = "날짜", example = "2024-01-10T00:00:00")
        LocalDateTime date,

        @Schema(description = "설명", example = "민족의 대명절")
        String description
) {
    public static HolidayResponse from(Holiday holiday) {
        return new HolidayResponse(
                holiday.getName(),
                holiday.getDate().atStartOfDay(),
                holiday.getDescription()
        );
    }
}
