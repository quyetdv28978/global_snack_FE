package com.example.snack_backend.core.khachhang.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginPayLoad {
    @NotBlank(message = "Username hoặc Email không được để trống")
    private String usernameOrEmail;

    @NotBlank(message = "Password không được để trống")
    private String password;
}

