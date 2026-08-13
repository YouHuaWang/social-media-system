package com.socialmedia.backend.dto.response;

public record AuthResponse(
    Long userId,
    String message,
    boolean requiresOtp,
    String tocken
) {
}
