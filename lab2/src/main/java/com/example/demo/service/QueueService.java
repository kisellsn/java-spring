package com.example.demo.service;

import com.example.demo.model.Queue;
import com.example.demo.model.QueueEntry;
import com.example.demo.model.User;

import java.util.List;

public interface QueueService {

    void createQueue(String name, String ownerName, int ownerId);

    void joinQueue(Queue queue, User user);

    List<Queue> getAllQueues();

    List<QueueEntry> getQueueEntriesByQueue(Queue queue);

    void removeQueueEntry(Queue queue, User user);

    void removeNextEntry(Queue queue);

    void closeQueue(Queue queue);

    Queue getQueueByID(int id);

    List<QueueEntry> getUserEntries(String userName);

    List<Queue> getUserQueues(int userID);

    void setLocked(Queue queue, boolean isLocked);
}
