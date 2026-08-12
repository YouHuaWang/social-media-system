package com.socialmedia.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginRequest(

        @NotBlank(message = "請輸入手機號碼")
        @Pattern(
                regexp = "^09\\d{8}$",
                message = "請輸入正確之手機號碼格式，如：0912345678"
        )
        String phone,

        @NotBlank(message = "請輸入密碼")
        String password
) {
}