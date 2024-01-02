package com.example.jdbcdemo.utils;

import com.example.jdbcdemo.queue.QueueEntity;
import com.example.jdbcdemo.queue.dtos.QueueByIdDTO;
import com.example.jdbcdemo.queue.dtos.QueueDTO;
import com.example.jdbcdemo.queue.dtos.QueueInUserDataDTO;
import com.example.jdbcdemo.user.UserEntity;
import com.example.jdbcdemo.user.dtos.PlaceInQueueDTO;
import com.example.jdbcdemo.user.dtos.UserDTO;
import com.example.jdbcdemo.user.dtos.UserIdDTO;

import java.util.List;

public class DtoConverter {
    public static UserDTO toUserDTO(UserEntity userEntity) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserID(userEntity.getUserID());
        userDTO.setLogin(userEntity.getLogin());
        return userDTO;
    }

    public static UserIdDTO toUserIdDTO(UserEntity userEntity, List<PlaceInQueueDTO> places, List<QueueInUserDataDTO> queues) {
        UserIdDTO userDTO = new UserIdDTO();

        userDTO.setLogin(userEntity.getLogin());
        userDTO.setUserID(userEntity.getUserID());
        userDTO.setPlaces(places);
        userDTO.setQueues(queues);
        return userDTO;
    }

    public static PlaceInQueueDTO toPlaceInQueueDTO(QueueEntity queue, UserEntity user) {
        PlaceInQueueDTO placeInQueueDTO = new PlaceInQueueDTO();
        placeInQueueDTO.setQueueName(queue.getName());
        placeInQueueDTO.setQueueID(queue.getQueueID());

        int place = queue.getUsers().indexOf(user) + 1;
        placeInQueueDTO.setPlace(place);
        return placeInQueueDTO;
    }

    public static QueueDTO toQueueDTO(QueueEntity queueEntity) {
        QueueDTO queueDTO = new QueueDTO();
        queueDTO.setQueueID(queueEntity.getQueueID());
        queueDTO.setName(queueEntity.getName());
        queueDTO.setOwnerName(queueEntity.getOwner().getLogin());
        queueDTO.setUsersNumber(queueEntity.getUsers().size());
        return queueDTO;
    }

    public static QueueByIdDTO toQueueIdDTO(QueueEntity queueEntity) {
        QueueByIdDTO queueDTO = new QueueByIdDTO();
        queueDTO.setQueueID(queueEntity.getQueueID());
        queueDTO.setName(queueEntity.getName());
        queueDTO.setLocked(queueEntity.isLocked());
        queueDTO.setOwnerName(queueEntity.getOwner().getLogin());

        List<UserDTO> users = queueEntity.getUsers()
                .stream()
                .map(DtoConverter::toUserDTO).toList();
        queueDTO.setUsers(users);
        return queueDTO;
    }

    public static QueueInUserDataDTO toQueueInUserDataDTO(QueueEntity queueEntity) {
        QueueInUserDataDTO queueDTO = new QueueInUserDataDTO();
        queueDTO.setQueueID(queueEntity.getQueueID());
        queueDTO.setName(queueEntity.getName());
        queueDTO.setUsersNumber(queueEntity.getUsers().size());
        queueDTO.setLocked(queueEntity.isLocked());
        return queueDTO;
    }
}
