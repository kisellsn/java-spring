package com.example.jdbcdemo.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer> {
    Optional<UserEntity> findByLogin(String login);

    @Query("SELECT u FROM UserEntity u JOIN u.queues q WHERE q.queueID = :queueID")
    List<UserEntity> findUsersByQueueId(@Param("queueID") int queueID);

    @Query(name = "UserEntity.findAllOwners")
    List<UserEntity> getAllOwners();

    Boolean existsDistinctByLogin(String login);
}
