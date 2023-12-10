package com.example.demo.repositories;

import com.example.demo.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryFake implements UserRepository {
    private final List<User> users = new ArrayList<>();

    public User save(User user) {
        users.add(user);
        return user;
    }

    public List<User> findAll() {
        return users;
    }

    public void deleteById(Long id) {
        Optional<User> userToDelete =  users.stream().filter(user -> user.getId().equals(id)).findFirst();
        if (userToDelete.isPresent()) {
            users.remove(userToDelete.get());
        }
    }

    public User findById(Long id) {
        Optional<User> userToFind =  users.stream().filter(user -> user.getId().equals(id)).findFirst();
        return userToFind.orElse(null);
    }

    public User find(String name) {
        Optional<User> userToFind =  users.stream().filter(user -> user.getName().equals(name)).findFirst();
        return userToFind.orElse(null);
    }
}
