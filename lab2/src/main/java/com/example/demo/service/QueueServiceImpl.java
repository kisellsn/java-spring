package com.example.demo.service;
import com.example.demo.model.Queue;
import com.example.demo.model.QueueEntry;
import com.example.demo.model.User;
import com.example.demo.repositories.RepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class QueueServiceImpl implements QueueService{

    private final RepositoryInterface<Queue> queueRepository;

    @Autowired
    public QueueServiceImpl(RepositoryInterface<Queue> queueRepository) {
        this.queueRepository = queueRepository;
    }

    public void createQueue(String name, String ownerName, int ownerID) {
        Queue queue = new Queue(name, ownerName, ownerID);
        queueRepository.save(queue);
    }

    public void joinQueue(Queue queue, User user) {
        if(!queue.isLocked()){
            QueueEntry entry = new QueueEntry(queue, user);
            queue.addEntry(entry);
        }
    }

    public List<Queue> getAllQueues() {
        return queueRepository.findAll();
    }

    public List<QueueEntry> getQueueEntriesByQueue(Queue queue) {
        return queue.getQueueEntries();
    }

    public void removeQueueEntry(Queue queue,  User user) {
        List<QueueEntry> queueEntries = queue.getQueueEntries();
        queueEntries.removeIf(entry -> entry.getUser().equals(user));
        IntStream.range(0, queueEntries.size()).forEach(i -> queueEntries.get(i).setId(i + 1));

    }
    public void removeNextEntry(Queue queue) {
        queue.removeEntry();
        if (queue.getQueueEntries().isEmpty()) {
            this.closeQueue(queue);
        }
    }

    public void closeQueue(Queue queue) {
        queueRepository.delete(queue);
    }

    public Queue getQueueByID(int id) {
        return queueRepository.findById(id);
    }

    public List<QueueEntry> getUserEntries(String userLogin) {
        List<Queue> queues = queueRepository.findAll();
        List<QueueEntry> entries = new ArrayList<>();
        for (Queue queue : queues) {
            for (QueueEntry entry : queue.getQueueEntries()) {
                String currUserLogin = entry.getUser().getLogin();
                if (currUserLogin.equals(userLogin)) {
                    entries.add(entry);
                }
            }
        }
        return entries;
    }

    public List<Queue> getUserQueues(int userID) {
        List<Queue> queues = queueRepository.findAll();
        List<Queue> userQueues = new ArrayList<>();
        for (Queue queue : queues) {
            if (queue.getOwnerID()==userID) {
                userQueues.add(queue);
            }
        }
        return userQueues;
    }

    public void setLocked(Queue queue, boolean isLocked){
        queue.setLocked(isLocked);
    }
}