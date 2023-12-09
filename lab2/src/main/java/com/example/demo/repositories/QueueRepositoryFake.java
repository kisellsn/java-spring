package com.example.demo.repositories;

import com.example.demo.model.Queue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QueueRepositoryFake implements QueueRepository {

    private final List<Queue> queues = new ArrayList<>();
    //private List<QueueEntry> queueEntries = new ArrayList<>();

    // Створення черги
    public Queue saveQueue(Queue queue) {
        queue.setId((long) (queues.size() + 1));
        queues.add(queue);
        return queue;
    }

    // Отримання всіх черг
    public List<Queue> getAllQueues() {
        return queues;
    }


    // Закриття черги
    public void closeQueue(Queue queue) {
        queues.remove(queue);
    }

    // Отримання черги
    public Optional<Queue> getQueueById(Long id) {
        return queues.stream().filter(queue -> queue.getId().equals(id)).findFirst();
    }
    public Optional<Queue> getQueueByName(String name) {
        return queues.stream().filter(queue -> queue.getName().equals(name)).findFirst();
    }

}
