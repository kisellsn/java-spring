package com.example.demo.model;

import java.util.concurrent.ThreadLocalRandom;

public class User {
    private final Long id;
    private final String name;
    private final String password;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        this.id = ThreadLocalRandom.current().nextLong(1,9999);
    }

    public String getName() {
        return this.name;
    }

    public String getPassword() {
        return this.password;
    }

    public Long getId() {
        return this.id;
    }
}
