package com.example.jdbcdemo.queue.dtos;

public class QueueCreationDTO {
    private String name;

    private String code;

    private int ownerID;

    QueueCreationDTO() {};

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public int getOwnerID() {
        return ownerID;
    }
}
