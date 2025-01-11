package com.kongdak.global.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "API 응답")
public class BaseResponse<T> {
    @Schema(example = "200", description = "HTTP 상태 코드")
    private final int status;

    @Schema(description = "성공 여부")
    private final boolean success;

    @Schema(description = "응답 데이터 (성공시에만 포함)")
    private final T data;

    @Schema(description = "에러 정보 (실패시에만 포함)")
    private final Error error;

    private BaseResponse(int status, boolean success, T data, Error error) {
        this.status = status;
        this.success = success;
        this.data = data;
        this.error = error;
    }

    public static <T> BaseResponse<T> ok(T data) {
        return new BaseResponse<>(200, true, data, null);
    }
    public static <T> BaseResponse<T> ok() {
        return new BaseResponse<>(200, true, null, null);
    }

    public static <T> BaseResponse<T> created(T data) {
        return new BaseResponse<>(201, true, data, null);
    }

    public static BaseResponse<Void> created() {
        return new BaseResponse<>(201, true, null, null);
    }
    public static <T> BaseResponse<T> error(int status, String code, String message) {
        return new BaseResponse<>(status, false, null, new Error(code, message));
    }

    @Getter
    public static class Error {
        private final String code;
        private final String message;

        public Error(String code, String message) {
            this.code = code;
            this.message = message;
        }
    }
}
