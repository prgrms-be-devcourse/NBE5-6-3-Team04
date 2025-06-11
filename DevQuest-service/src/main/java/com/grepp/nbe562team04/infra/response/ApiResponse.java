package com.grepp.nbe562team04.infra.response;

public record ApiResponse<T>(
        String code,
        String message,
        T data
) {

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(ResponseCode.OK.code(), ResponseCode.OK.message(), data);
    }

    public static <T> ApiResponse<T> noContent() {
        return new ApiResponse<>(ResponseCode.OK.code(), ResponseCode.OK.message(), null);
    }

    public static <T> ApiResponse<T> error(ResponseCode code) {
        return new ApiResponse<>(code.code(), code.message(), null);
    }

    public static <T> ApiResponse<T> error(ResponseCode code, T data) {
        return new ApiResponse<>(code.code(), code.message(), data);
    }
}
