package com.socialmedia.backend.dto.response;

public record CommentResponse(
    Long commentId,
    Long postId,
    Long userId,
    String message
) {
}