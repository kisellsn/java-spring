package com.example.jdbcdemo.queue;

import com.example.jdbcdemo.user.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface QueueRepository extends CrudRepository<QueueEntity, Integer> {

    List<QueueEntity> findByNameContaining(String name);

    List<QueueEntity> findByOwner(UserEntity owner);
}
