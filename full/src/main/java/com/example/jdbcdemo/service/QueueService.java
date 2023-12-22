package com.example.jdbcdemo.service;

import com.example.jdbcdemo.models.Queue;
import com.example.jdbcdemo.models.User;

import java.util.List;
import java.util.Map;

public interface QueueService {

    int add(String name, String code, boolean isLocked, int ownerID);
    void update(String name, String code, boolean isLocked, int ownerID, int queueID);

    //void joinQueue(Queue queue, User user);
    void joinQueue(int queueID, int userID);

    List<Queue> getAllQueues();

    List<User> getAllUsersInQueue(int queueID);

    void removeQueueEntry(int queueID, int userID, String code);

    void removeNextEntry(int queueID, String code);

    void closeQueue(int queueID, String code);

    Queue getQueueByID(int id);

    List<Map<String, Object>> getUserEntries(int userID);

    List<Queue> getUserQueues(int userID);

    void setLocked(int queueID, boolean isLocked);
}
