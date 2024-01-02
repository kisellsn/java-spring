package com.example.jdbcdemo.models;

public class Queue {
    private int queueID;
    private String name;
    private String code;
    private boolean isLocked;
    private int ownerID;

    public Queue() {}

    public Queue(String name, String code, boolean isLocked, int ownerID) {
        this.name = name;
        this.code = code;
        this.isLocked = isLocked;
        this.ownerID = ownerID;
    }

    public Queue(int queueID, String name, String code, boolean isLocked, int ownerID) {
        this.queueID = queueID;
        this.name = name;
        this.code = code;
        this.isLocked = isLocked;
        this.ownerID = ownerID;
    }

    public int getQueueID() {
        return queueID;
    }

    public void setQueueID(int queueID) {
        this.queueID = queueID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        this.isLocked = locked;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }
}
