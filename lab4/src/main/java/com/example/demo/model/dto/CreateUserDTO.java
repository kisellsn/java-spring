package com.example.demo.model.dto;

public class CreateUserDTO {
    private String name;
    private String password;

    public CreateUserDTO() {
    }

    public CreateUserDTO(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
