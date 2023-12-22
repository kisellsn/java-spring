package com.example.jdbcdemo.repository;

import com.example.jdbcdemo.models.User;

import java.util.List;

public interface IRelationship {
    void joinQueue(int queueID, int userID);
    int getPlace(int queueID, int userID);
    void deleteHead(int queueID);
    void deleteInQueueByID(int queueID, int userID);
    List<User> getAllUsersInQueue(int queueID);
    List<User> getAllOwners();
}
