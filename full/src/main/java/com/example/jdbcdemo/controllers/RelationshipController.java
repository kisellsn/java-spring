package com.example.jdbcdemo.controllers;

import com.example.jdbcdemo.DAOs.RelationshipDAO;
import com.example.jdbcdemo.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/relationship")
public class RelationshipController {
    @Autowired
    RelationshipDAO relationshipDB;

    @GetMapping()
    public ResponseEntity<List<User>> getAllOwners() {
        try {
            List<User> users = new ArrayList<>(relationshipDB.getAllOwners());
            if (users.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{queueID}")
    public ResponseEntity<List<User>> getAllUsersInQueue(@PathVariable("queueID") int queueID) {
        try {
            List<User> users = new ArrayList<>(relationshipDB.getAllUsersInQueue(queueID));
            if (users.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{userID}/{queueID}")
    public ResponseEntity<String> joinUserQueue(@PathVariable("userID") int userID, @PathVariable("queueID") int queueID) {
        relationshipDB.joinQueue(queueID, userID);
        return new ResponseEntity<>("User was added successfully to Queue.", HttpStatus.CREATED);
    }

    @GetMapping("/{userID}/{queueID}")
    public ResponseEntity<String> getPlace(@PathVariable("userID") int userID, @PathVariable("queueID") int queueID) {
        int place = relationshipDB.getPlace(queueID, userID);  // если возвращает 0, то его там нет
        return new ResponseEntity<>("Place = "+place, HttpStatus.OK);
    }

    @DeleteMapping("/{queueID}")
    public ResponseEntity<String> deleteHeadOfQueue(@PathVariable("queueID") int queueID) {
        try {
            relationshipDB.deleteHead(queueID);
            return new ResponseEntity<>("Deleted Head successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Cannot delete Queues.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{userID}/{queueID}")
    public ResponseEntity<String> deleteUserInQueue(@PathVariable("userID") int userID, @PathVariable("queueID") int queueID) {
        try {
            relationshipDB.deleteInQueueByID(queueID, userID);
            return new ResponseEntity<>("Queue was deleted successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Cannot delete Queue.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
