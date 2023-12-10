package com.example.demo.repositories;

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

    public User save(User user) {
        users.add(user);
        return user;
    }

    public List<User> findAll() {
        return users;
    }

    public void delete(User user) {
        users.remove(user);
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
