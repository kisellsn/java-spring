package com.example.jdbcdemo.service;

import com.example.jdbcdemo.models.User;
import com.example.jdbcdemo.repository.IRepository;
import com.example.jdbcdemo.repository.IRelationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    private IRepository<User> userRepository;

    @Autowired
    public void setUserRepository(IRepository<User> userRepository) {
        this.userRepository = userRepository;
    }

    //@Transactional
    public int add(String login, String password) {
        User user = this.getUserByName(login);
        if (user == null) {
            User newUser = new User(login, password);
           return userRepository.add(newUser);
        }
        return 0;
    }

    public void update(String login, String password, int userID) {
        User user = userRepository.findById(userID);
        if (user != null) {
            user.setLogin(login);
            user.setPassword(password);
            userRepository.update(user);
        }
    }

    public User getUser(int id) {
        return this.userRepository.findById(id);
    }

    public void deleteUser(User user) {
        this.userRepository.deleteById(user.getUserID());
    }

    public User getUserByName(String name) {
        return this.userRepository.findByName(name);
    }
}
