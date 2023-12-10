package com.example.demo.service;

import com.example.demo.model.User;

public interface UserService {

    User createUser(String name, String password);

    User getUser(Long id);

    void deleteUser(Long id);

    User getUserByName(String name);
}
