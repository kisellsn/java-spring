package com.example.demo.service;
import com.example.demo.model.Page;
import com.example.demo.model.Queue;
import com.example.demo.model.User;
import com.example.demo.repositories.IRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QueueServiceImpl implements IQueueService {

    private final IRepositoryInterface<Queue> queueRepository;

    @Autowired
    private final IUserService userService;

    @Autowired
    public QueueServiceImpl(IRepositoryInterface<Queue> queueRepository, IUserService userService) {
        this.queueRepository = queueRepository;
        this.userService = userService;
    }

    public boolean createQueue(String name, String ownerName) {
        User user = userService.getUserByName(ownerName);
        if (!InputValidator.isValidInput(name) || user == null) {
            return false;
        }

        Queue queue = new Queue(name, user.getId());
        queueRepository.save(queue);

        return true;
    }

    public boolean joinQueue(Queue queue, String userName) {
        User user = userService.getUserByName(userName);

        if(user == null)
        {
            return false;
        }

        queue.addToQueue(user);

        return true;
    }

    public List<Queue> getAllQueues() {
        return queueRepository.findAll();
    }

    public List<User> getQueueEntriesByQueue(Queue queue) {
        return queue.getQueueEntries();
    }

    public void removeQueueEntry(Queue queue, String userName) {
        List<User> queueEntries = queue.getQueueEntries();
        queueEntries.removeIf(entry -> entry.getName().equals(userName));
    }

    public void removeNextEntry(Queue queue) {
        queue.removeLast();
    }

    public void closeQueue(Queue queue) {
        queue.setLocked(true);
    }


    public Queue getQueueByName(String name) {
        return queueRepository.find(name);
    }

    public Queue getQueueById(Long queueId) {
        return queueRepository.findById(queueId);
    }

    public List<User> getUserEntries(String userName) {
        List<Queue> queues = queueRepository.findAll();
        List<User> entries = new ArrayList<>();
        for (Queue queue : queues) {
            for (User entry : queue.getQueueEntries()) {
                if (entry.getName().equals(userName)) {
                    entries.add(entry);
                }
            }
        }
        return entries;
    }

    public List<Queue> getUserQueues(Long userId) {
        List<Queue> queues = queueRepository.findAll();
        List<Queue> userQueues = new ArrayList<>();
        for (Queue queue : queues) {
            if (queue.getOwnerId().equals(userId)) {
                userQueues.add(queue);
            }
        }
        return userQueues;
    }

    public Page<Queue> findQueuesByOwnerNameWithPagination(Long ownerId, int pageNumber, int pageSize) {
        List<Queue> allQueues = getAllQueues();
        List<Queue> filteredQueues = allQueues.stream()
                .filter(queue -> queue.getId().equals(ownerId))
                .collect(Collectors.toList());

        int totalElements = filteredQueues.size();
        int fromIndex = pageNumber * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalElements);

        if (fromIndex > totalElements || fromIndex < 0) {
            return new Page<>(Collections.emptyList(), pageNumber, pageSize, totalElements);
        }

        List<Queue> paginatedList = filteredQueues.subList(fromIndex, toIndex);
        return new Page<>(paginatedList, pageNumber, pageSize, totalElements);
    }

}