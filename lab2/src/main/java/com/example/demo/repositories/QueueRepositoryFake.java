package com.example.demo.repositories;

import com.example.demo.model.Queue;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Scope("prototype")
public class QueueRepositoryFake implements QueueRepository {

    private final List<Queue> queues = new ArrayList<>();

    public Queue save(Queue queue) {
        queue.setId((long) (queues.size() + 1));
        queues.add(queue);
        return queue;
    }

    public List<Queue> findAll() {
        return queues;
    }

    public void delete(Queue queue) {
        queues.remove(queue);
    }

    public Optional<Queue> findById(Long id) {
        return queues.stream().filter(queue -> queue.getId().equals(id)).findFirst();
    }
    public Optional<Queue> find(String name) {
        return queues.stream().filter(queue -> queue.getName().equals(name)).findFirst();
    }

}
