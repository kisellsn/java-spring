package com.example.demo.service;

import com.example.demo.model.User;

public interface UserService {

    void add(String login, String password);
    void update(String login, String password, int userID);

    User getUser(int id);

    void deleteUser(User user);

    User getUserByName(String name);
}
