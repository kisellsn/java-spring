package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repositories.IRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements IUserService {

    private IRepositoryInterface<User> userRepository;

    @Autowired
    public void setUserRepository(IRepositoryInterface<User> userRepository) {
        this.userRepository = userRepository;
    }

    public boolean createUser(String name, String password) {
        if (!InputValidator.isValidInput(name) || !InputValidator.isValidInput(password)) {
            return false;
        }
        User user = getUserByName(name);
        if (user != null) {
            return false;
        }
        user = new User(name, password);
        userRepository.save(user);

        return true;
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public void deleteUser(User user) {
        this.userRepository.delete(user);
    }

    public User getUserByName(String name) {
        return this.userRepository.find(name);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id);
    }
}
