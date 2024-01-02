package com.example.demo.controller.controllerApi;

import com.example.demo.model.Page;
import com.example.demo.model.Queue;
import com.example.demo.model.User;
import com.example.demo.model.dto.CreateQueueDTO;
import com.example.demo.model.dto.GetPagedUsersQueuesDTO;
import com.example.demo.model.dto.GetQueueEntryDTO;
import com.example.demo.service.IQueueService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/queues")
public class QueueController {

    private final IQueueService queueService;

    @Autowired
    public QueueController(IQueueService queueService) {
        this.queueService = queueService;
    }

    @PostMapping
    public ResponseEntity<Queue> createQueue(@RequestBody CreateQueueDTO createQueueDTO) {
        if(!queueService.createQueue(
                createQueueDTO.getName(), createQueueDTO.getOwnerName())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{queueName}/join")
    public ResponseEntity<?> joinQueue(@PathVariable String queueName,
                                       @RequestParam String userName) {
        Optional<Queue> queue = Optional.ofNullable(queueService.getQueueByName(queueName));
        if (queue.isPresent()) {
            queueService.joinQueue(queue.get(), userName);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Queue>> getAllQueues() {
        return ResponseEntity.ok(queueService.getAllQueues());
    }

    @GetMapping("/{queueName}/entries")
    public ResponseEntity<List<User>> getQueueEntries(@PathVariable String queueName) {
        Optional<Queue> queue = Optional.ofNullable(queueService.getQueueByName(queueName));
        return queue.map(value -> ResponseEntity.ok(queueService.getQueueEntriesByQueue(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{queueName}/entries")
    public ResponseEntity<?> removeQueueEntry(@PathVariable String queueName,
                                              @RequestParam String userName) {
        Optional<Queue> queue = Optional.ofNullable(queueService.getQueueByName(queueName));
        if (queue.isPresent()) {
            queueService.removeQueueEntry(queue.get(), userName);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{queueId}/next")
    public ResponseEntity<?> removeNextEntry(@PathVariable String queueId) {
        Optional<Queue> queue = Optional.ofNullable(queueService.getQueueByName(queueId));
        if (queue.isPresent()) {
            queueService.removeNextEntry(queue.get());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{queueName}")
    public ResponseEntity<?> closeQueue(@PathVariable String queueName) {
        Optional<Queue> queue = Optional.ofNullable(queueService.getQueueByName(queueName));
        if (queue.isPresent()) {
            queueService.closeQueue(queue.get());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/searchByOwner")
    @ResponseBody
    public ResponseEntity<Page<Queue>> getQueuesByOwnerName(GetPagedUsersQueuesDTO pagedUsersQueuesDTO) {
        Page<Queue> queuesPage = queueService.findQueuesByOwnerNameWithPagination(
                pagedUsersQueuesDTO.getOwnerId(), pagedUsersQueuesDTO.getPage(), pagedUsersQueuesDTO.getSize());
        return ResponseEntity.ok(queuesPage);
    }
}
