package com.innervix.model3d.common;

public record ApiResponse<T>(boolean success, T data, String message) {

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static ApiResponse<Void> ok() {
        return new ApiResponse<>(true, null, null);
    }

    public static ApiResponse<Void> failed(String message) {
        return new ApiResponse<>(false, null, message);
    }
}
