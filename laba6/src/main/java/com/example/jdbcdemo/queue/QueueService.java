package com.example.jdbcdemo.queue;

import com.example.jdbcdemo.queue.dtos.QueueByIdDTO;
import com.example.jdbcdemo.queue.dtos.QueueCreationDTO;
import com.example.jdbcdemo.queue.dtos.QueueDTO;
import com.example.jdbcdemo.user.UserEntity;
import com.example.jdbcdemo.user.UserRepository;
import com.example.jdbcdemo.utils.DtoConverter;
import com.example.jdbcdemo.utils.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class QueueService {
    private final QueueRepository queueRepository;

    private final UserRepository userRepository;

    @Autowired
    public QueueService(QueueRepository queueRepository, UserRepository userRepository) {
        this.queueRepository = queueRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ResponseHandler<List<QueueDTO>> getAllQueues() {
        Iterable<QueueEntity> queuesIterable = queueRepository.findAll();
        List<QueueEntity> queueEntities = new ArrayList<>();
        queuesIterable.forEach(queueEntities::add);

        List<QueueDTO> queues = queueEntities.stream()
                .filter(queueEntity -> !queueEntity.isLocked())
                .map(DtoConverter::toQueueDTO)
                .toList();

        ResponseHandler<List<QueueDTO>> responseHandler = new ResponseHandler<>();
        responseHandler.setBody(queues);
        responseHandler.setStatusCode(HttpStatus.OK);

        return responseHandler;
    }

    @Transactional
    public ResponseHandler<QueueDTO> addQueue(QueueCreationDTO queueDTO, String password) {
        UserEntity owner = userRepository.findById(queueDTO.getOwnerID()).orElse(null);
        ResponseHandler<QueueDTO> responseHandler = new ResponseHandler<>();
        if (owner == null) {
            responseHandler.setMessage("Cannot find user with id=" + queueDTO.getOwnerID());
            responseHandler.setStatusCode(HttpStatus.BAD_REQUEST);
            return responseHandler;
        }
        if (!owner.getPassword().equals(password)) {
            responseHandler.setMessage("Wrong password");
            responseHandler.setStatusCode(HttpStatus.UNAUTHORIZED);
            return responseHandler;
        }

        QueueEntity queueEntity = new QueueEntity();
        queueEntity.setName(queueDTO.getName());
        queueEntity.setCode(queueDTO.getCode());
        queueEntity.setOwner(owner);
        QueueEntity savedQueue = queueRepository.save(queueEntity);

        QueueDTO savedQueueDTO = DtoConverter.toQueueDTO(savedQueue);
        responseHandler.setBody(savedQueueDTO);
        responseHandler.setStatusCode(HttpStatus.CREATED);
        return responseHandler;
    }

    public ResponseHandler<QueueByIdDTO> findQueueById(int id) {
        QueueEntity queueEntity = queueRepository.findById(id).orElse(null);
        ResponseHandler<QueueByIdDTO> responseHandler = new ResponseHandler<>();
        if (queueEntity == null) {
            responseHandler.setMessage("Cannot find queue with id=" + id);
            responseHandler.setStatusCode(HttpStatus.NOT_FOUND);
            return responseHandler;
        }
        QueueByIdDTO queue = DtoConverter.toQueueIdDTO(queueEntity);
        responseHandler.setBody(queue);
        responseHandler.setStatusCode(HttpStatus.OK);
        return responseHandler;
    }

    public ResponseHandler<List<QueueDTO>> findQueuesByName(String name) {
        List<QueueDTO> queues = queueRepository.findByNameContaining(name)
                .stream().map(DtoConverter::toQueueDTO)
                .toList();
        ResponseHandler<List<QueueDTO>> responseHandler = new ResponseHandler<>();
        responseHandler.setBody(queues);
        responseHandler.setStatusCode(HttpStatus.OK);
        return responseHandler;
    }

    public ResponseHandler<List<QueueDTO>> findQueuesByOwner(int ownerID) {
        UserEntity owner = userRepository.findById(ownerID).orElse(null);
        ResponseHandler<List<QueueDTO>> responseHandler = new ResponseHandler<>();
        if (owner == null) {
            responseHandler.setMessage("Cannot find user with id=" + ownerID);
            responseHandler.setStatusCode(HttpStatus.NOT_FOUND);
            return responseHandler;
        }
        List<QueueDTO> queues = queueRepository.findByOwner(owner).stream()
                .map(DtoConverter::toQueueDTO).toList();

        responseHandler.setBody(queues);
        responseHandler.setStatusCode(HttpStatus.OK);
        return responseHandler;
    }

    @Transactional
    public ResponseHandler<?> deleteQueueById(int id, String code) {
        ResponseHandler<?> responseHandler = new ResponseHandler<>();
        QueueEntity queue = queueRepository.findById(id).orElse(null);
        if (queue == null) {
            responseHandler.setMessage("Cannot find queue with id=" + id);
            responseHandler.setStatusCode(HttpStatus.NOT_FOUND);
            return responseHandler;
        }
        if (!queue.getCode().equals(code)) {
            responseHandler.setMessage("Wrong queue code");
            responseHandler.setStatusCode(HttpStatus.UNAUTHORIZED);
            return responseHandler;
        }
        queueRepository.deleteById(id);
        responseHandler.setMessage("Queue was deleted successfully");
        responseHandler.setStatusCode(HttpStatus.OK);
        return responseHandler;
    }

    @Transactional
    public ResponseHandler<?> closeQueue(int id, String code) {
        QueueEntity queue = queueRepository.findById(id).orElse(null);
        ResponseHandler<?> responseHandler = new ResponseHandler<>();
        if (queue == null) {
            responseHandler.setMessage("Cannot find queue with id=" + id);
            responseHandler.setStatusCode(HttpStatus.NOT_FOUND);
            return responseHandler;
        }
        if (!queue.getCode().equals(code)) {
            responseHandler.setMessage("Wrong queue code");
            responseHandler.setStatusCode(HttpStatus.UNAUTHORIZED);
            return responseHandler;
        }
        if (queue.getUsers().isEmpty()) {
            queueRepository.deleteById(id);
            responseHandler.setMessage("Queue is closed and deleted");
            responseHandler.setStatusCode(HttpStatus.OK);
            return responseHandler;
        }
        queue.setLocked(true);
        queueRepository.save(queue);
        responseHandler.setMessage("Queue is closed");
        responseHandler.setStatusCode(HttpStatus.OK);
        return responseHandler;
    }

    @Transactional
    public ResponseHandler<?> openQueue(int id, String code) {
        QueueEntity queue = queueRepository.findById(id).orElse(null);
        ResponseHandler<?> responseHandler = new ResponseHandler<>();
        if (queue == null) {
            responseHandler.setMessage("Cannot find queue with id=" + id);
            responseHandler.setStatusCode(HttpStatus.NOT_FOUND);
            return responseHandler;
        }
        if (!queue.getCode().equals(code)) {
            responseHandler.setMessage("Wrong queue code");
            responseHandler.setStatusCode(HttpStatus.UNAUTHORIZED);
            return responseHandler;
        }
        queue.setLocked(false);
        queueRepository.save(queue);
        responseHandler.setMessage("Queue is opened");
        responseHandler.setStatusCode(HttpStatus.OK);
        return responseHandler;
    }
}
