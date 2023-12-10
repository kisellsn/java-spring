package com.example.demo.service;

import com.example.demo.model.Queue;
import com.example.demo.model.QueueEntry;

import java.util.List;
import java.util.Optional;

public interface QueueService {

    Queue createQueue(String name, String ownerName, Long ownerId);

    void joinQueue(Queue queue, String userName);

    List<Queue> getAllQueues();

    List<QueueEntry> getQueueEntriesByQueue(Queue queue);

    void removeQueueEntry(Queue queue, String userName);

    void removeNextEntry(Queue queue);

    void closeQueue(Queue queue);

    Queue getQueueByName(String name);

    List<QueueEntry> getUserEntries(String userName);

    List<Queue> getUserQueues(String userName);
}
