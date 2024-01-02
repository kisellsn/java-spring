package com.example.jdbcdemo.relation;

import com.example.jdbcdemo.queue.QueueRepository;
import com.example.jdbcdemo.user.UserEntity;
import com.example.jdbcdemo.queue.QueueEntity;
import com.example.jdbcdemo.user.UserRepository;
import com.example.jdbcdemo.user.dtos.PlaceInQueueDTO;
import com.example.jdbcdemo.utils.DtoConverter;
import com.example.jdbcdemo.utils.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RelationService {
    private final UserRepository userRepository;
    private final QueueRepository queueRepository;

    @Autowired
    public RelationService(UserRepository userRepository, QueueRepository queueRepository) {
        this.userRepository = userRepository;
        this.queueRepository = queueRepository;
    }

    @Transactional
    public ResponseHandler<PlaceInQueueDTO> joinUserQueue(int userID, int queueID) {
        UserEntity user = userRepository.findById(userID).orElse(null);
        QueueEntity queue = queueRepository.findById(queueID).orElse(null);
        ResponseHandler<PlaceInQueueDTO> responseHandler = new ResponseHandler<>();
        if (user == null || queue == null) {
            responseHandler.setMessage((user == null ? "User" : "Queue") + " does not exist");
            responseHandler.setStatusCode(HttpStatus.NOT_FOUND);
            return responseHandler;
        }
        if (queue.isLocked()) {
            responseHandler.setMessage("Queue is locked");
            responseHandler.setStatusCode(HttpStatus.CONFLICT);
            return responseHandler;
        }
        if (user.getQueues().contains(queue)) {
            responseHandler.setMessage("User is already in the queue");
            responseHandler.setStatusCode(HttpStatus.CONFLICT);
            return responseHandler;
        }

        queue.addUser(user);
        responseHandler.setBody(DtoConverter.toPlaceInQueueDTO(queue, user));
        responseHandler.setStatusCode(HttpStatus.OK);
        return responseHandler;
    }

    @Transactional
    public Boolean deleteUserFromQueue(UserEntity user, QueueEntity queue) {
        if (!user.getQueues().contains(queue)) {
            return false;
        }
        queue.deleteUser(user);
        if (queue.getUsers().isEmpty() && queue.isLocked()) {
            queueRepository.deleteById(queue.getQueueID());
        }
        return true;
    }

    @Transactional
    public ResponseHandler<?> removeUser(int userID, int queueID, String code) {
        UserEntity user = userRepository.findById(userID).orElse(null);
        QueueEntity queue = queueRepository.findById(queueID).orElse(null);
        ResponseHandler<?> responseHandler = new ResponseHandler<>();
        if (user == null || queue == null) {
            responseHandler.setMessage((user == null ? "User" : "Queue") + " does not exist");
            responseHandler.setStatusCode(HttpStatus.NOT_FOUND);
            return responseHandler;
        }
        if (!queue.getCode().equals(code)) {
            responseHandler.setMessage("Wrong queue code");
            responseHandler.setStatusCode(HttpStatus.UNAUTHORIZED);
            return responseHandler;
        }
        boolean toDelete = queue.getUsers().size() == 1;
        Boolean result = deleteUserFromQueue(user, queue);
        if (!result) {
            responseHandler.setMessage("The user is not in the queue");
            responseHandler.setStatusCode(HttpStatus.NOT_FOUND);
            return responseHandler;
        }
        responseHandler.setMessage(
                !toDelete
                ? "User removed from queue successfully"
                : "Last user removed from queue successfully and queue is deleted"
        );
        responseHandler.setStatusCode(HttpStatus.OK);
        return responseHandler;
    }

    @Transactional
    public ResponseHandler<?> exitFromQueue(int userID, int queueID, String password) {
        UserEntity user = userRepository.findById(userID).orElse(null);
        QueueEntity queue = queueRepository.findById(queueID).orElse(null);
        ResponseHandler<?> responseHandler = new ResponseHandler<>();
        if (user == null || queue == null) {
            responseHandler.setMessage((user == null ? "User" : "Queue") + " does not exist");
            responseHandler.setStatusCode(HttpStatus.NOT_FOUND);
            return responseHandler;
        }
        if (!user.getPassword().equals(password)) {
            responseHandler.setMessage("Wrong password");
            responseHandler.setStatusCode(HttpStatus.UNAUTHORIZED);
            return responseHandler;
        }
        boolean toDelete = queue.getUsers().size() == 1;
        Boolean result = deleteUserFromQueue(user, queue);
        if (!result) {
            responseHandler.setMessage("The user is not in the queue");
            responseHandler.setStatusCode(HttpStatus.NOT_FOUND);
            return responseHandler;
        }
        responseHandler.setMessage(
                !toDelete
                        ? "User exited from queue successfully"
                        : "Last user exited from queue successfully and queue is deleted"
        );
        responseHandler.setStatusCode(HttpStatus.OK);
        return responseHandler;
    }

    @Transactional
    public ResponseHandler<?> deleteHeadOfQueue(int queueID, String code) {
        QueueEntity queue = queueRepository.findById(queueID).orElse(null);
        ResponseHandler<?> responseHandler = new ResponseHandler<>();
        if (queue == null) {
            responseHandler.setMessage("Queue does not exist");
            responseHandler.setStatusCode(HttpStatus.NOT_FOUND);
            return responseHandler;
        }
        if (!queue.getCode().equals(code)) {
            responseHandler.setMessage("Wrong queue code");
            responseHandler.setStatusCode(HttpStatus.UNAUTHORIZED);
            return responseHandler;
        }
        if (queue.getUsers().isEmpty()) {
            responseHandler.setMessage("Queue is empty");
            responseHandler.setStatusCode(HttpStatus.CONFLICT);
            return responseHandler;
        }
        UserEntity firstUser = queue.getUsers().getFirst();
        boolean toDelete = queue.getUsers().size() == 1;
        deleteUserFromQueue(firstUser, queue);
        responseHandler.setMessage(
                !toDelete
                ? "Head of the queue removed successfully"
                : "Last user removed from queue successfully and queue is deleted"
        );
        responseHandler.setStatusCode(HttpStatus.OK);
        return responseHandler;
    }

    public ResponseHandler<PlaceInQueueDTO> getPlace(int userID, int queueID) {
        QueueEntity queue = queueRepository.findById(queueID).orElse(null);
        UserEntity user = userRepository.findById(userID).orElse(null);
        ResponseHandler<PlaceInQueueDTO> responseHandler = new ResponseHandler<>();
        if (user == null || queue == null) {
            responseHandler.setMessage((user == null ? "User" : "Queue") + " does not exist");
            responseHandler.setStatusCode(HttpStatus.NOT_FOUND);
            return responseHandler;
        }
        if (!queue.getUsers().contains(user)) {
            responseHandler.setMessage("The user is not in the queue");
            responseHandler.setStatusCode(HttpStatus.NOT_FOUND);
            return responseHandler;
        }
        PlaceInQueueDTO placeInQueueDTO = DtoConverter.toPlaceInQueueDTO(queue, user);
        responseHandler.setBody(placeInQueueDTO);
        responseHandler.setStatusCode(HttpStatus.OK);
        return responseHandler;
    }
}
