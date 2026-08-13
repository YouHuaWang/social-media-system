package com.socialmedia.backend.common;

public record ApiResponse<T>(
    int status,
    String message,
    T data
) {

    public static <T> ApiResponse<T> success(
        int status,
        String message,
        T data
    ) {
        return new ApiResponse<>(
            status,
            message,
            data
        );
    }

    public static <T> ApiResponse<T> fail(
        int status,
        String message
    ) {
        return new ApiResponse<>(
            status,
            message,
            null
        );
    }
}