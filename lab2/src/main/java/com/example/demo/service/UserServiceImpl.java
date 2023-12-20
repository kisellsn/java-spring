package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.RepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    private RepositoryInterface<User> userRepository;

    @Autowired
    public void setUserRepository(RepositoryInterface<User> userRepository) {
        this.userRepository = userRepository;
    }

    public void add(String login, String password) {
        User user = this.getUserByName(login);
        if (user == null) {
            User newUser = new User(login, password);
            userRepository.add(newUser);
        }

    }

    public void update(String login, String password, int userID) {
        User user = userRepository.findById(userID);
        if (user != null) {
            user.setUserID(userID);
            user.setLogin(login);
            user.setPassword(password);
            //userRepository.update(queue);
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
