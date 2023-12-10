package com.example.demo.model;


public class QueueEntry {
    private Long id;
    private final String userName;
    private Queue queue;

    public QueueEntry(Queue queue, String userName) {
        this.queue = queue;
        this.userName = userName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Queue getQueue() {
        return queue;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }

    public String getUserName() {
        return userName;
    }

}
