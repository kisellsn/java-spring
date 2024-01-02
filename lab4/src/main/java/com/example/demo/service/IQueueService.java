package com.example.demo.service;

import com.example.demo.model.Page;
import com.example.demo.model.Queue;
import com.example.demo.model.User;

import java.util.List;

public interface IQueueService {

    boolean createQueue(String name, String ownerName);

    boolean joinQueue(Queue queue, String userName);

    List<Queue> getAllQueues();

    List<User> getQueueEntriesByQueue(Queue queue);

    void removeQueueEntry(Queue queue, String userName);

    void removeNextEntry(Queue queue);

    void closeQueue(Queue queue);

    Queue getQueueByName(String name);

    Queue getQueueById(Long id);

    List<User> getUserEntries(String userName);

    List<Queue> getUserQueues(Long userId);

    Page<Queue> findQueuesByOwnerNameWithPagination(Long ownerId, int pageNumber, int pageSize);
}
