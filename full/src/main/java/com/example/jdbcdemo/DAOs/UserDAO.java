package com.example.jdbcdemo.DAOs;

import com.example.jdbcdemo.models.User;
import com.example.jdbcdemo.repository.IRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAO implements IRepository<User> {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int add(User user) {
        return jdbcTemplate.update("INSERT INTO [User] (login, password) VALUES (?,?)",
                user.getLogin(), user.getPassword());
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update("UPDATE [User] SET login=?, password=? WHERE userID=?",
                user.getLogin(), user.getPassword(), user.getUserID());
    }

    @Override
    public User findById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM [User] WHERE userID=?",
                BeanPropertyRowMapper.newInstance(User.class), id);
    }

    @Override
    public User findByName(String name) {
        return jdbcTemplate.queryForObject("SELECT * FROM [User] WHERE login=?",
                BeanPropertyRowMapper.newInstance(User.class), name);
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM [User]", BeanPropertyRowMapper.newInstance(User.class));
    }

    @Override
    public int deleteById(int id) {
        return jdbcTemplate.update("BEGIN TRANSACTION; " +
                "DECLARE @UserId INT; " +
                "SET @UserId = ?; " +
                "DELETE FROM PlaceInQueue WHERE userID = @UserId; "+
                "DELETE FROM PlaceInQueue " +
                "WHERE queueID IN (SELECT queueID FROM Queue WHERE ownerID = @UserId); "+
                "DELETE FROM Queue WHERE ownerID = @UserId; " +
                "DELETE FROM [User] WHERE userID = @UserId; " +
                "IF @@ERROR = 0 COMMIT TRANSACTION; " +
                "ELSE ROLLBACK TRANSACTION;", id);
    }

    @Override
    public int deleteAll() {
        return jdbcTemplate.update("DELETE FROM [User]");
    }
}
