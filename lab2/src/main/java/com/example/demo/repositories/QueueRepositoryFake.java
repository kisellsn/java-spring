package com.example.demo.repositories;

import com.example.demo.model.Queue;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Scope("prototype")
public class QueueRepositoryFake implements RepositoryInterface<Queue> {

    private final List<Queue> queues = new ArrayList<>();

    public Queue save(Queue queue) {
        queue.setQueueID((queues.size() + 1));
        queues.add(queue);
        return queue;
    }

    public List<Queue> findAll() {
        return queues;
    }

    public void delete(Queue queue) {
        queues.remove(queue);
    }

    public Queue findById(int id) {
        Optional<Queue> queueToFind =  queues.stream().filter(queue -> queue.getQueueID() == id).findFirst();
        return queueToFind.orElse(null);
    }

    public Queue find(String name) {
        Optional<Queue> queueToFind =  queues.stream().filter(queue -> queue.getName().equals(name)).findFirst();
        return queueToFind.orElse(null);
    }

}
