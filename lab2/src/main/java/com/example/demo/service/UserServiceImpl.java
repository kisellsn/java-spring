package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(String name, String password) {
        User user = new User(name, password);
        return userRepository.save(user);
    }

    public User getUser(Long id) {
        return this.userRepository.findById(id);
    }

    public void deleteUser(Long id) {
        this.userRepository.deleteById(id);
    }

    public User getUserByName(String name) {
        return this.userRepository.find(name);
    }
}
