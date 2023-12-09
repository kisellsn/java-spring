package com.example.demo.repositories;

import com.example.demo.model.Queue;

import java.util.List;
import java.util.Optional;

public interface QueueRepository {
    Queue saveQueue(Queue queue);

    List<Queue> getAllQueues();

    void closeQueue(Queue queue);

    Optional<Queue> getQueueById(Long id);

    Optional<Queue> getQueueByName(String name);
}
