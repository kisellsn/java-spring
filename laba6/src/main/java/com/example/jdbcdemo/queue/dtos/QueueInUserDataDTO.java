package com.example.jdbcdemo.queue.dtos;

public class QueueInUserDataDTO {
    private int queueID;

    private String name;

    private boolean isLocked;

    private int usersNumber;

    public QueueInUserDataDTO() {};

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

    public boolean getIsLocked() {
        return isLocked;
    }

    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public void setUsersNumber(int usersNumber) {
        this.usersNumber = usersNumber;
    }

    public int getUsersNumber() {
        return usersNumber;
    }
}
