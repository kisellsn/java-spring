package com.example.demo.service;

import com.example.demo.model.Queue;
import com.example.demo.model.QueueEntry;
import com.example.demo.model.User;

import java.util.List;

public interface QueueService {

    void add(String name, String code, boolean isLocked, int ownerID);
    void update(String name, String code, boolean isLocked, int ownerID, int queueID);

    void joinQueue(Queue queue, User user);

    List<Queue> getAllQueues();

    List<QueueEntry> getQueueEntriesByQueue(int queueID);

    void removeQueueEntry(int queueID, User user, String code);

    void removeNextEntry(int queueID, String code);

    void closeQueue(int queueID, String code);

    Queue getQueueByID(int id);

    List<QueueEntry> getUserEntries(String userName);

    List<Queue> getUserQueues(int userID);

    void setLocked(int queueID, boolean isLocked);
}
