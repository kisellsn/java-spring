package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Scope("prototype")
public class UserRepositoryFake implements RepositoryInterface<User> {
    private final List<User> users = new ArrayList<>();

    public int add(User user) {
        users.add(user);
        return user.getUserID();
    }
    public void update(User user) {
        int index = users.indexOf(user);
        if (index != -1) {
            users.set(index, user);
        }
    }

    public List<User> findAll() {
        return users;
    }

    public void deleteById(int userID) {
        users.removeIf(user -> user.getUserID() == userID);
    }

    public User findById(int id) {
        Optional<User> userToFind =  users.stream().filter(user -> user.getUserID()==id).findFirst();
        return userToFind.orElse(null);
    }
    public User findByName(String name) {
        Optional<User> userToFind =  users.stream().filter(user -> user.getLogin().equals(name)).findFirst();
        return userToFind.orElse(null);
    }
}
