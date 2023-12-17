package com.example.demo.model;


public class QueueEntry {
    private int id;
    private final String userName;
    private Queue queue;

    public QueueEntry(Queue queue, String userName) {
        this.queue = queue;
        this.userName = userName;
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

    public String getUserName() {
        return userName;
    }

}
