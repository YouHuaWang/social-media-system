package com.socialmedia.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record VerifyOtpRequest(
    @NotBlank(message = "請輸入手機號碼")
    @Pattern(
        regexp = "^09\\d{8}$",
        message = "請輸入正確之手機號碼格式，如：0912345678"
    )
    String phone,

    @NotBlank(message = "請輸入驗證碼")
    @Pattern(
        regexp = "^\\d{6}$",
        message = "驗證碼須為6位數字"
    )
    String otp
) {
}
