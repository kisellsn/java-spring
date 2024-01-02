package com.example.demo.service;

import com.example.demo.model.User;

public interface IUserService {

    boolean createUser(String name, String password);

    void deleteUser(User user);

    User getUserByName(String name);

    User getUserById(Long id);
}
