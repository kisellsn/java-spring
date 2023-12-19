package com.example.demo.model;

import java.util.concurrent.ThreadLocalRandom;

public class User {
    private int userID;
    private String login;
    private String password;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
        this.userID = ThreadLocalRandom.current().nextInt(1,9999);
    }
//
//    public User(String login, String password) {
//        this.login = login;
//        this.password = password;
//    }
//
//    public User(int userID, String login, String password) {
//        this.userID = userID;
//        this.login = login;
//        this.password = password;
//    }

    public User() {
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserID() {
        return userID;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }



}
