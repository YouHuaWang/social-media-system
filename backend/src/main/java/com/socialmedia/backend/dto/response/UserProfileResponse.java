package com.socialmedia.backend.dto.response;

public record UserProfileResponse(
    Long userId,
    String userName,
    String phone,
    String email,
    Boolean phoneVerified,
    Boolean emailVerified,
    String coverImage,
    String biography
) {
}