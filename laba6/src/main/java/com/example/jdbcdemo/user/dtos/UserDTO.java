package com.example.jdbcdemo.user.dtos;

public class UserDTO {
    private int userID;

    private String login;

    public UserDTO() {};

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getUserID() {
        return userID;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }
}
