package com.example.jdbcdemo.user.dtos;

public class UserUpdateDTO {
    private int userID;

    private String login;

    public UserUpdateDTO() {};

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
