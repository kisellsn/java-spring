package com.example.jdbcdemo.DAOs;

import com.example.jdbcdemo.models.Queue;
import com.example.jdbcdemo.models.User;
import com.example.jdbcdemo.repository.IRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QueueDAO implements IRepository<Queue> {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int add(Queue queue) {
        return jdbcTemplate.update("INSERT INTO Queue (isLocked, name, code, ownerID) VALUES (?,?,?,?)",
                queue.isLocked(), queue.getName(), queue.getCode(), queue.getOwnerID());
    }

    @Override
    public void update(Queue queue) {
        jdbcTemplate.update("UPDATE Queue SET name=?, code=?, isLocked=?, ownerID=? WHERE queueID=?",
                queue.getName(), queue.getCode(), queue.isLocked(), queue.getOwnerID(), queue.getQueueID());
    }

    @Override
    public Queue findById(int id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM Queue WHERE queueID=?",
                    BeanPropertyRowMapper.newInstance(Queue.class), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Queue findByName(String name) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM Queue WHERE name=?",
                    BeanPropertyRowMapper.newInstance(Queue.class), name);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Queue> findAll() {
        return jdbcTemplate.query("SELECT * FROM Queue", BeanPropertyRowMapper.newInstance(Queue.class));
    }

    @Override
    public int deleteById(int id) {
        return jdbcTemplate.update("DELETE FROM Queue WHERE queueID=?", id);
    }

    @Override
    public int deleteAll() {
        return jdbcTemplate.update("DELETE FROM Queue");
    }
}
