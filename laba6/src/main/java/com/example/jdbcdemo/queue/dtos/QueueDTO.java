package com.example.jdbcdemo.queue.dtos;

public class QueueDTO {
    private int queueID;

    private String name;

    private String ownerName;

    private int usersNumber;

    public QueueDTO() {};

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

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setUsersNumber(int usersNumber) {
        this.usersNumber = usersNumber;
    }

    public int getUsersNumber() {
        return usersNumber;
    }
}
