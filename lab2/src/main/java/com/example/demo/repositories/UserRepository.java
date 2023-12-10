package com.example.demo.repositories;

import com.example.demo.model.User;

import java.util.List;

public interface UserRepository {

    User save(User user);

    List<User> findAll();

    void deleteById(Long id);

    User findById(Long id);
}
