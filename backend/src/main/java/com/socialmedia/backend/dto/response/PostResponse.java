package com.socialmedia.backend.dto.response;

import java.time.LocalDateTime;

public record PostResponse(
    Long postId,
    Long userId,
    String userName,
    String content,
    String image,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}