package com.example.demo.repositories;

import com.example.demo.model.Queue;

import java.util.List;
import java.util.Optional;

public interface QueueRepository {
    Queue save(Queue queue);

    List<Queue> findAll();

    void delete(Queue queue);

    Optional<Queue> findById(Long id);

    Optional<Queue> find(String name);
}
