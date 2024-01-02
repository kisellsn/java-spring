package com.example.jdbcdemo.user;

import com.example.jdbcdemo.queue.QueueRepository;
import com.example.jdbcdemo.queue.dtos.QueueInUserDataDTO;
import com.example.jdbcdemo.user.dtos.*;
import com.example.jdbcdemo.utils.DtoConverter;
import com.example.jdbcdemo.utils.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final QueueRepository queueRepository;

    @Autowired
    public UserService(UserRepository userRepository, QueueRepository queueRepository) {
        this.userRepository = userRepository;
        this.queueRepository = queueRepository;
    }

    public ResponseHandler<List<UserDTO>> getAllUsers() {
        Iterable<UserEntity> usersIterable = userRepository.findAll();
        List<UserEntity> userEntities = new ArrayList<>();
        usersIterable.forEach(userEntities::add);

        List<UserDTO> userDTOS = userEntities.stream()
                .map(DtoConverter::toUserDTO)
                .toList();
        ResponseHandler<List<UserDTO>> responseHandler = new ResponseHandler<>();
        responseHandler.setBody(userDTOS);
        responseHandler.setStatusCode(HttpStatus.OK);
        return responseHandler;
    }

    @Transactional
    public ResponseHandler<UserDTO> addUser(UserRegisterDTO user) {
        ResponseHandler<UserDTO> responseHandler = new ResponseHandler<>();
        if (userRepository.existsDistinctByLogin(user.getLogin())) {
            responseHandler.setMessage("There is a user with this login");
            responseHandler.setStatusCode(HttpStatus.CONFLICT);
            return responseHandler;
        }
        UserEntity newUser = new UserEntity();
        newUser.setLogin(user.getLogin());
        newUser.setPassword(user.getPassword());

        UserDTO userDTO = DtoConverter.toUserDTO(userRepository.save(newUser));
        responseHandler.setBody(userDTO);
        responseHandler.setStatusCode(HttpStatus.CREATED);
        return responseHandler;
    }

    public ResponseHandler<UserIdDTO> findUserById(int userID) {
        UserEntity user = userRepository.findById(userID).orElse(null);
        ResponseHandler<UserIdDTO> messageHandler = new ResponseHandler<>();
        if (user == null) {
            messageHandler.setMessage("Cannot find User with id=" + userID);
            messageHandler.setStatusCode(HttpStatus.NOT_FOUND);
            return messageHandler;
        }

        List<QueueInUserDataDTO> ownedQueues = user.getOwnedQueues().stream()
                .map(DtoConverter::toQueueInUserDataDTO).toList();
        List<PlaceInQueueDTO> places = user.getQueues().stream()
                .map(queueEntity -> DtoConverter.toPlaceInQueueDTO(queueEntity, user)).toList();
        UserIdDTO userIdDTO = DtoConverter.toUserIdDTO(user, places, ownedQueues);
        messageHandler.setBody(userIdDTO);
        messageHandler.setStatusCode(HttpStatus.OK);
        return messageHandler;
    }

    public ResponseHandler<UserDTO> findUserByLogin(String login) {
        UserEntity user = userRepository.findByLogin(login).orElse(null);
        ResponseHandler<UserDTO> responseHandler = new ResponseHandler<>();
        if (user == null) {
            responseHandler.setMessage("User is not found");
            responseHandler.setStatusCode(HttpStatus.NOT_FOUND);
            return responseHandler;
        }
        responseHandler.setBody(DtoConverter.toUserDTO(user));
        responseHandler.setStatusCode(HttpStatus.OK);
        return responseHandler;
    }

    @Transactional
    public ResponseHandler<UserDTO> updateUser(UserUpdateDTO userDTO, String password) {
        ResponseHandler<UserDTO> responseHandler = new ResponseHandler<>();
        if (userRepository.existsDistinctByLogin(userDTO.getLogin())) {
            responseHandler.setMessage("There is a user with this login");
            responseHandler.setStatusCode(HttpStatus.CONFLICT);
            return responseHandler;
        }
        UserEntity user = userRepository.findById(userDTO.getUserID()).orElse(null);
        if (user == null) {
            responseHandler.setMessage("Cannot find User with id=" + userDTO.getUserID());
            responseHandler.setStatusCode(HttpStatus.NOT_FOUND);
            return responseHandler;
        }
        if (!Objects.equals(user.getPassword(), password)) {
            responseHandler.setMessage("Wrong password");
            responseHandler.setStatusCode(HttpStatus.UNAUTHORIZED);
            return responseHandler;
        }
        user.setLogin(userDTO.getLogin());
        UserDTO updatedUser = DtoConverter.toUserDTO(userRepository.save(user));
        responseHandler.setBody(updatedUser);
        responseHandler.setStatusCode(HttpStatus.OK);
        return responseHandler;
    }

    @Transactional
    public ResponseHandler<?> deleteUserById(int userID, String password) {
        ResponseHandler<?> responseHandler = new ResponseHandler<>();
        UserEntity user = userRepository.findById(userID).orElse(null);
        if (user == null) {
            responseHandler.setMessage("Cannot find User with id=" + userID);
            responseHandler.setStatusCode(HttpStatus.NOT_FOUND);
            return responseHandler;
        }
        if (!Objects.equals(user.getPassword(), password)) {
            responseHandler.setMessage("Wrong password");
            responseHandler.setStatusCode(HttpStatus.UNAUTHORIZED);
            return responseHandler;
        }
        userRepository.deleteById(userID);
        responseHandler.setMessage("User was deleted successfully");
        responseHandler.setStatusCode(HttpStatus.OK);
        return responseHandler;
    }

    public ResponseHandler<List<UserDTO>> getUsersByQueue(int queueID) {
        ResponseHandler<List<UserDTO>> responseHandler = new ResponseHandler<>();
        if (!queueRepository.existsById(queueID)) {
            responseHandler.setMessage("Cannot find queue with id=" + queueID);
            responseHandler.setStatusCode(HttpStatus.NOT_FOUND);
            return responseHandler;
        }
        List<UserDTO> users = userRepository.findUsersByQueueId(queueID).stream()
                .map(DtoConverter::toUserDTO)
                .toList();
        responseHandler.setBody(users);
        responseHandler.setStatusCode(HttpStatus.OK);
        return responseHandler;
    }

    public ResponseHandler<List<UserDTO>> getAllOwners() {
        ResponseHandler<List<UserDTO>> responseHandler = new ResponseHandler<>();
        List<UserDTO> owners = userRepository.getAllOwners().stream().map(DtoConverter::toUserDTO).toList();
        responseHandler.setBody(owners);
        responseHandler.setStatusCode(HttpStatus.OK);
        return responseHandler;
    }
}
