package com.example.jdbcdemo.user.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class UserRegisterDTO {
    @NotEmpty
    @Size(min = 3, max = 20, message = "Login must be at least 3 characters and maximum 20 characters ")
    private String login;

    @NotEmpty
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    public UserRegisterDTO() {}

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
