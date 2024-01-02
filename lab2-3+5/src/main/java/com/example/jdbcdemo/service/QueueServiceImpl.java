package com.example.jdbcdemo.service;
import com.example.jdbcdemo.models.Queue;
import com.example.jdbcdemo.models.User;
import com.example.jdbcdemo.repository.IRepository;
import com.example.jdbcdemo.repository.IRelationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QueueServiceImpl implements QueueService{

    private final IRepository<Queue> queueRepository;
    private final IRelationship relationshipRepository;
    private final UserService userService;

    @Autowired
    public QueueServiceImpl(IRepository<Queue> queueRepository, IRelationship relationshipRepository, UserService userService) {
        this.queueRepository = queueRepository;
        this.relationshipRepository = relationshipRepository;
        this.userService = userService;
    }

    public int add(String name, String code, boolean isLocked, int ownerID) {
        Queue queue = queueRepository.findByName(name);
        if (queue == null) {
            Queue newQueue = new Queue(name, code, isLocked, ownerID);
            return queueRepository.add(newQueue);
        }
        return 0;
    }

    public void update(String name, String code, boolean isLocked, int ownerID, int queueID) {
        Queue queue = queueRepository.findById(queueID);
        Queue existingQueueWithSameName = queueRepository.findByName(name);
        if (queue != null && existingQueueWithSameName == null) {
            queue.setName(name);
            queue.setCode(code);
            queue.setLocked(isLocked);
            System.out.println(queue.isLocked());
            System.out.println(isLocked);
            queue.setOwnerID(ownerID);
            queueRepository.update(queue);
        }
    }

    public void joinQueue(int queueID, int userID) {
        User user = userService.getUser(userID);
        Queue queue = queueRepository.findById(queueID);;
        if(user!=null && queue!=null && !queue.isLocked()){
            relationshipRepository.joinQueue(queue.getQueueID(), user.getUserID());
        }
    }

    public List<Queue> getAllQueues() {
        return queueRepository.findAll();
    }

    public List<User> getAllUsersInQueue(int queueID) {
        Queue queue = this.getQueueByID(queueID);
        if (queue != null) {
            return relationshipRepository.getAllUsersInQueue(queueID);
        }
        return null;
    }

    public void removeQueueEntry(int queueID,  int userID, String code) {
        Queue queue = queueRepository.findById(queueID);;
        User user = userService.getUser(userID);
        if (queue!=null && user!=null && queue.getCode().equals(code)) {
            relationshipRepository.deleteInQueueByID(queueID, userID);
        }
    }
    public void removeNextEntry(int queueID, String code) {
        Queue queue = queueRepository.findById(queueID);;
        if (queue != null) {
            relationshipRepository.deleteHead(queueID);
            if (relationshipRepository.getAllUsersInQueue(queueID).isEmpty()) {
                this.closeQueue(queueID, code);
            }
        }
    }

    public void closeQueue(int queueID, String code) {
        Queue queue = queueRepository.findById(queueID);;
        if (queue != null && queue.getCode().equals(code)) {
            queueRepository.deleteById(queue.getQueueID());
        }
    }

    public Queue getQueueByID(int id) {
        return queueRepository.findById(id);
    }

    public List<Map<String, Object>> getUserEntries(int userID) {
        List<Queue> queues = queueRepository.findAll();
        List<Map<String, Object>> entries = new ArrayList<>();
        for (Queue queue : queues) {
            List<User> usersInQueue = relationshipRepository.getAllUsersInQueue(queue.getQueueID());
            User requestedUser = usersInQueue.stream()
                    .filter(user -> user.getUserID() == userID)
                    .findFirst()
                    .orElse(null);
            if (requestedUser != null) {
                int place = relationshipRepository.getPlace(queue.getQueueID(), requestedUser.getUserID());

                Map<String, Object> entryMap = new HashMap<>();
                entryMap.put("userID", requestedUser.getUserID());
                entryMap.put("userLogin", requestedUser.getLogin());
                entryMap.put("queueID", queue.getQueueID());
                entryMap.put("queueName", queue.getName());
                entryMap.put("place", place);
                entries.add(entryMap);
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
        Queue queue = queueRepository.findById(queueID);
        if (queue != null) {
            queue.setLocked(isLocked);
            queueRepository.update(queue);
        }
    }

    public String deleteAll() {
        try {
            int numRows = queueRepository.deleteAll();
            return String.valueOf(new ResponseEntity<>("Deleted " + numRows + " Queue(s) successfully.", HttpStatus.OK));
        } catch (Exception e) {
            return String.valueOf(new ResponseEntity<>("Cannot delete Queues.", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
}