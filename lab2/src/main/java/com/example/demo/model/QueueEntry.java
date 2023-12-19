package com.example.demo.model;


public class QueueEntry {
    private int id;
    //private final String userLogin;
    private Queue queue;
    private User user;

    public QueueEntry(Queue queue, User user) {
        this.queue = queue;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Queue getQueue() {
        return queue;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

}
