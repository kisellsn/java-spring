package com.example.jdbcdemo.user.dtos;

import com.example.jdbcdemo.queue.dtos.QueueInUserDataDTO;

import java.util.List;

public class UserIdDTO {
    private int userID;

    private String login;

    private List<PlaceInQueueDTO> places;

    private List<QueueInUserDataDTO> queues;

    public UserIdDTO() {};

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getUserID() {
        return userID;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setPlaces(List<PlaceInQueueDTO> places) {
        this.places = places;
    }

    public List<PlaceInQueueDTO> getPlaces() {
        return places;
    }

    public void setQueues(List<QueueInUserDataDTO> queues) {
        this.queues = queues;
    }

    public List<QueueInUserDataDTO> getQueues() {
        return queues;
    }
}
