package com.example.demo.model;


import java.util.ArrayList;
import java.util.List;

public class Queue {
    private Long id;
    private String name;
    private Long ownerId;

    private boolean isLocked;

    private final List<User> queueEntries = new ArrayList<>();

    public Queue() {
    }

    public List<User> getQueueEntries(){
        return queueEntries;
    }

    public Queue(String name, Long ownerId) {
        this.name = name;
        this.ownerId = ownerId;
    }

    public void addToQueue(User user){
        queueEntries.add(user);
    }

    public void removeLast(){
        queueEntries.remove(queueEntries.size() - 1);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }
}

