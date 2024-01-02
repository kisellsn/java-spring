package com.example.jdbcdemo.controllers;

import com.example.jdbcdemo.DAOs.UserDAO;
import com.example.jdbcdemo.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    UserDAO userDB;

    @GetMapping()
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = new ArrayList<>(userDB.findAll());
            if (users.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping()
    public ResponseEntity<String> createUser(@RequestBody User user) {
        try {
            userDB.add(new User(user.getLogin(), user.getPassword()));
            return new ResponseEntity<>("User was added successfully.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{userID}")
    public ResponseEntity<User> getUserById(@PathVariable("userID") int userID) {
        User user = userDB.findById(userID);
        if (user != null) return new ResponseEntity<>(user, HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/name/{login}")
    public ResponseEntity<User> getUserByName(@PathVariable("login") String login) {
        User user = userDB.findByName(login);
        if (user != null) return new ResponseEntity<>(user, HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping()
    public ResponseEntity<String> updateUser(@RequestBody User newUser) {
        User user = userDB.findById(newUser.getUserID());
        if (user != null) {
            user.setLogin(newUser.getLogin());
            user.setPassword(newUser.getPassword());

            userDB.update(user);
            return new ResponseEntity<>("User was updated successfully.", HttpStatus.OK);
        }
        return new ResponseEntity<>("Cannot find User with id=" + newUser.getUserID(), HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{userID}")
    public ResponseEntity<String> deleteUser(@PathVariable("userID") int userID) {
        int result = userDB.deleteById(userID);
        if (result == 0) return new ResponseEntity<>("Cannot find User with id=" + userID, HttpStatus.OK);
        return new ResponseEntity<>("User was deleted successfully.", HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteAllUsers() {
        try {
            int numRows = userDB.deleteAll();
            return new ResponseEntity<>("Deleted " + numRows + " User(s) successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Cannot delete users.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
