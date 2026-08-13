package com.socialmedia.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCommentRequest(

    @NotBlank(message = "留言內容不可為空白")
    @Size(max = 1000, message = "留言內容不可超過1000個字")
    String content

) {
}