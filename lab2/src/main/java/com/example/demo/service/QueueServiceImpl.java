package com.example.demo.service;
import com.example.demo.model.Queue;
import com.example.demo.model.QueueEntry;
import com.example.demo.repositories.QueueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QueueServiceImpl implements QueueService{

    private final QueueRepository queueRepository;

    @Autowired
    public QueueServiceImpl(QueueRepository queueRepository) {
        this.queueRepository = queueRepository;
    }
//    @Autowired
//    public void setQueueRepository(QueueRepositoryStub queueRepository) {
//        this.queueRepository = queueRepository;
//    }

    public Queue createQueue(String name, String ownerName) {
        Queue queue = new Queue(name, ownerName);
        return queueRepository.saveQueue(queue);
    }

    public void joinQueue(Queue queue, String userName) {
        QueueEntry entry = new QueueEntry(queue, userName);
        queue.addEntry(entry);
    }

    public List<Queue> getAllQueues() {
        return queueRepository.getAllQueues();
    }

    public List<QueueEntry> getQueueEntriesByQueue(Queue queue) {
        return queue.getQueueEntries();
    }

    public void removeQueueEntry(Queue queue, String userName) {
        queue.getQueueEntries().removeIf(entry -> entry.getUserName().equals(userName));

    }
    public void removeNextEntry(Queue queue) {
        queue.removeEntry();
    }

    public void closeQueue(Queue queue) {
        queueRepository.closeQueue(queue);
    }


    public Optional<Queue> getQueueByName(String name) {
        return queueRepository.getQueueByName(name);
    }

    public List<QueueEntry> getUserEntries(String userName) {
        List<Queue> queues = queueRepository.getAllQueues();
        List<QueueEntry> entries = new ArrayList<>();
        for (Queue queue : queues) {
            for (QueueEntry entry : queue.getQueueEntries()) {
                if (entry.getUserName().equals(userName)) {
                    entries.add(entry);
                }
            }
        }
        return entries;
    }

    public List<Queue> getUserQueues(String userName) {
        List<Queue> queues = queueRepository.getAllQueues();
        List<Queue> userQueues = new ArrayList<>();
        for (Queue queue : queues) {
            if (queue.getOwnerName().equals(userName)) {
                userQueues.add(queue);
            }
        }
        return userQueues;
    }
}