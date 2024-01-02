package com.example.jdbcdemo.queue.dtos;

import com.example.jdbcdemo.user.dtos.UserDTO;

import java.util.List;

public class QueueByIdDTO {
    private int queueID;

    private String name;

    private boolean isLocked;

    private String ownerName;

    private List<UserDTO> users;

    public QueueByIdDTO() {};

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

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }

    public List<UserDTO> getUsers() {
        return users;
    }
}
