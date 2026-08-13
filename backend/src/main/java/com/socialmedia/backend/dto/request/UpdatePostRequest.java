package com.socialmedia.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdatePostRequest(

    @NotBlank(message = "貼文內容不可為空白")
    @Size(max = 2000, message = "貼文內容不可超過2000個字")
    String content,

    @Size(max = 500, message = "圖片網址不可超過500個字")
    String image

) {
}