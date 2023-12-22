package com.example.jdbcdemo.DAOs;

import com.example.jdbcdemo.models.User;
import com.example.jdbcdemo.repository.IRelationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RelationshipDAO implements IRelationship {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void joinQueue(int queueID, int userID) {
        jdbcTemplate.update("INSERT INTO PlaceInQueue (queueID, userID) VALUES (?,?)", queueID, userID);
    }

    @Override
    public int getPlace(int queueID, int userID) {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM PlaceInQueue WHERE queueID = ? " +
                "AND recordID <= (SELECT recordID FROM PlaceInQueue WHERE queueID = ? AND userID = ?)",
                new Object[]{queueID, queueID, userID}, Integer.class);
    }

    @Override
    public void deleteHead(int queueID) {
        jdbcTemplate.update("DELETE FROM PlaceInQueue " +
                "WHERE recordID = (SELECT MIN(recordID) FROM PlaceInQueue WHERE queueID=?)", queueID);
    }

    @Override
    public void deleteInQueueByID(int queueID, int userID) {
        jdbcTemplate.update("DELETE FROM PlaceInQueue WHERE queueID=? AND userID=?", queueID, userID);
    }

    @Override
    public List<User> getAllUsersInQueue(int queueID) {
        return jdbcTemplate.query("SELECT [User].* FROM [User], PlaceInQueue, Queue " +
                "WHERE [User].userID=PlaceInQueue.userID AND PlaceInQueue.queueID=Queue.queueID AND Queue.queueID=?",
                new BeanPropertyRowMapper<>(User.class), queueID);
    }

    @Override
    public List<User> getAllOwners() {
        return jdbcTemplate.query("SELECT DISTINCT [User].* FROM Queue, [User] WHERE userID=ownerID",
                new BeanPropertyRowMapper<>(User.class));
    }
}
