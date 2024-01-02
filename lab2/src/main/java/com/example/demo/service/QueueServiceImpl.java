package com.example.demo.service;
import com.example.demo.model.Queue;
import com.example.demo.model.QueueEntry;
import com.example.demo.model.User;
import com.example.demo.repository.RepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class QueueServiceImpl implements QueueService{

    private final RepositoryInterface<Queue> queueRepository;
    private final UserService userService;

    @Autowired
    public QueueServiceImpl(RepositoryInterface<Queue> queueRepository,  UserService userService) {
        this.queueRepository = queueRepository;
        this.userService = userService;
    }

    public void add(String name, String code, boolean isLocked, int ownerID) {
        Queue queue = new Queue(name, code, isLocked, ownerID);
        queueRepository.add(queue);
    }

    public void update(String name, String code, boolean isLocked, int ownerID, int queueID) {
        Queue queue = queueRepository.findById(queueID);
        if (queue != null) {
            queue.setName(name);
            queue.setCode(code);
            queue.setLocked(isLocked);
            queue.setOwnerID(ownerID);
            queueRepository.update(queue);
        }
    }


    public void joinQueue(int queueID, int userID) {
        User user = userService.getUser(userID);
        Queue queue = queueRepository.findById(queueID);;
        if(user!=null && queue!=null && !queue.isLocked()){
            QueueEntry entry = new QueueEntry(queue, user);
            queue.addEntry(entry);
        }
    }

    public List<Queue> getAllQueues() {
        return queueRepository.findAll();
    }

    public List<QueueEntry> getQueueEntriesByQueue(int queueID) {
        Queue queue = this.getQueueByID(queueID);
        if (queue != null) {
            return queue.getQueueEntries();
        }
        return null;
    }

    public void removeQueueEntry(int queueID,  User user, String code) {
        Queue queue = this.getQueueByID(queueID);
        if (queue!=null && queue.getCode().equals(code)) {
            List<QueueEntry> queueEntries = queue.getQueueEntries();
            queueEntries.removeIf(entry -> entry.getUser().equals(user));
            IntStream.range(0, queueEntries.size()).forEach(i -> queueEntries.get(i).setId(i + 1));
        }

    }
    public void removeNextEntry(int queueID, String code) {
        Queue queue = this.getQueueByID(queueID);
        if (queue != null) {
            queue.removeEntry();
            if (queue.getQueueEntries().isEmpty()) {
                this.closeQueue(queueID, code);
            }
        }
    }

    public void closeQueue(int queueID, String code) {
        Queue queue = this.getQueueByID(queueID);
        if (queue != null && queue.getCode().equals(code)) {
            queueRepository.deleteById(queue.getQueueID());
        }
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

    public void setLocked(int queueID, boolean isLocked){
        Queue queue = this.getQueueByID(queueID);
        if (queue != null) {
            this.update(queue.getName(), queue.getCode(), isLocked, queue.getOwnerID(), queueID);
        }
    }
}