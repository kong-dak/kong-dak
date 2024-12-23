package com.kongdak.global.response;

import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private final int status;
    private final boolean success;
    private final T data;
    private final Error error;

    private ApiResponse(int status, boolean success, T data, Error error) {
        this.status = status;
        this.success = success;
        this.data = data;
        this.error = error;
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(200, true, data, null);
    }

    public static <T> ApiResponse<T> ok() {
        return new ApiResponse<>(200, true, null, null);
    }

    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(201, true, data, null);
    }

    public static <T> ApiResponse<T> error(int status, String code, String message) {
        return new ApiResponse<>(status, false, null, new Error(code, message));
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
