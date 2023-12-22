package com.example.jdbcdemo.controllers;

import com.example.jdbcdemo.DAOs.QueueDAO;
import com.example.jdbcdemo.DAOs.UserDAO;
import com.example.jdbcdemo.models.Queue;
import com.example.jdbcdemo.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/queues")
public class QueueController {
    @Autowired
    QueueDAO queueDB;
    @Autowired
    UserDAO userDB;

    @GetMapping()
    public ResponseEntity<List<Queue>> getAllQueues() {
        try {
            List<Queue> queues = new ArrayList<>(queueDB.findAll());
            if (queues.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(queues, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping()
    public ResponseEntity<String> createQueue(@RequestBody Queue queue) {
        queueDB.add(queue);
        return new ResponseEntity<>("Queue was added successfully.", HttpStatus.CREATED);
    }

    @GetMapping("/{queueID}")
    public ResponseEntity<Queue> getQueueById(@PathVariable("queueID") int queueID) {
        Queue queue = queueDB.findById(queueID);
        if (queue != null) return new ResponseEntity<>(queue, HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Queue> getQueueByName(@PathVariable("name") String name) {
        Queue queue = queueDB.findByName(name);
        if (queue != null) return new ResponseEntity<>(queue, HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{queueID}")
    public ResponseEntity<String> deleteQueue(@PathVariable("queueID") int queueID) {
        try {
            int result = queueDB.deleteById(queueID);
            if (result == 0) return new ResponseEntity<>("Cannot find Queue with id=" + queueID, HttpStatus.OK);
            return new ResponseEntity<>("Queue was deleted successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Cannot delete Queue.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteAllQueues() {
        try {
            int numRows = queueDB.deleteAll();
            return new ResponseEntity<>("Deleted " + numRows + " Queue(s) successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Cannot delete Queues.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
