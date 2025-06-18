package com.grepp.nbe563team04.infra.response;

import com.grepp.nbe563team04.app.controller.api.auth.payload.TokenResponse;

public record ApiResponse<T>(
        String code,
        String message,
        T data
) {

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(ResponseCode.OK.code(), ResponseCode.OK.message(), data);
    }

    public static <T> ApiResponse<T> error(ResponseCode code) {
        return new ApiResponse<>(code.code(), code.message(), null);
    }

    public static <T> ApiResponse<T> error(ResponseCode code, T data) {
        return new ApiResponse<>(code.code(), code.message(), data);
    }

    public static ApiResponse<TokenResponse> error() {
        return new ApiResponse<>("401", "UNAUTHORIZED", null);
    }
}
