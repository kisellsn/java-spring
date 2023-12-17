package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repositories.RepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    private RepositoryInterface<User> userRepository;

    @Autowired
    public void setUserRepository(RepositoryInterface<User> userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(String name, String password) {
        User user = new User(name, password);
        userRepository.save(user);
    }

    public User getUser(int id) {
        return this.userRepository.findById(id);
    }

    public void deleteUser(User user) {
        this.userRepository.delete(user);
    }

    public User getUserByName(String name) {
        return this.userRepository.find(name);
    }
}
