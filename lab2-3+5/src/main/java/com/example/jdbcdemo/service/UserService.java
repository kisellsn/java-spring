package com.example.jdbcdemo.service;

import com.example.jdbcdemo.models.User;

public interface UserService {

    int add(String login, String password);
    void update(String login, String password, int userID);

    User getUser(int id);

    void deleteUser(User user);

    User getUserByName(String name);
}
