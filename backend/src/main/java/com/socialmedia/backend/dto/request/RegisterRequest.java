package com.socialmedia.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

    @NotBlank(message = "請輸入使用者名稱")
    @Size(max = 50, message = "使用者名稱長度不可超過50個字")
    String userName,

    @NotBlank(message = "請輸入手機號碼")
    @Pattern(
        regexp = "^09\\d{8}$",
        message = "請輸入正確之手機號碼格式，如：0912345678"
    )
    String phone,

    @NotBlank(message = "請輸入Email")
    @Email(message = "請輸入正確之Email格式")
    @Size(max = 100, message = "Email長度不可超過100個字")
    String email,

    @NotBlank(message = "請輸入密碼")
    @Size(min = 8, max = 16, message = "密碼長度需介於8至16字元")
    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,16}$",
        message = "密碼需為8至16個字元，且至少包含英文與數字"
    )
    String password
) {
}