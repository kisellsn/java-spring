package com.example.jdbcdemo.user.dtos;

public class PlaceInQueueDTO {
    private int queueID;

    private String queueName;

    private int place;

    public PlaceInQueueDTO() {};

    public void setQueueID(int queueID) {
        this.queueID = queueID;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public void setQueueName(String name) {
        this.queueName = name;
    }

    public String getQueueName() {
        return queueName;
    }

    public int getPlace() {
        return place;
    }

    public int getQueueID() {
        return queueID;
    }
}
