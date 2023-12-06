package com.example.demo.controller;
import com.example.demo.model.Queue;
import com.example.demo.model.QueueEntry;
import com.example.demo.service.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;


@Controller
public class Lab2Controller {
    private final QueueService queueService;

    @Autowired
    public Lab2Controller(QueueService queueService) {
        this.queueService = queueService;
    }

    @GetMapping("/queues")
    public String showQueues(Model model) {
        List<Queue> queues = queueService.getAllQueues();
        model.addAttribute("queues", queues);
        return "queues";
    }

    @GetMapping("/createQueue")
    public String showCreateQueueForm() {
        return "create_queue";
    }
    @PostMapping("/createQueue")
    public String createQueue(String queueName, String ownerName, String password, Model model) {
        Queue newQueue = queueService.createQueue(queueName, ownerName, password);
        return "redirect:/queues";
    }
    @GetMapping("/joinQueue")
    public String showJoinQueueForm(@RequestParam String name, Model model) {
        queueService.getQueueByName(name).ifPresent(queue -> {
            model.addAttribute("queue", queue);
        });
        return "join_queue";
    }

    @PostMapping("/joinQueue")
    public String joinQueue(@RequestParam String name, String userName) {
        queueService.getQueueByName(name).ifPresent(queue -> {
            queueService.joinQueue(queue, userName);
        });
        return "redirect:/queue?name="+name;
    }

    @GetMapping("/queue")
    public String showQueueDetails(@RequestParam String name, Model model) {
        Optional<Queue> queue = queueService.getQueueByName(name);
        if (queue.isPresent()) {
            List<QueueEntry> entries = queueService.getQueueEntriesByQueue(queue.get());
            model.addAttribute("queue", queue.get());
            model.addAttribute("entries", entries);
            return "queue_details";
        } else {
            return "redirect:/queues";
        }
    }

    @GetMapping("/closeQueue")
    public String deleteQueue(@RequestParam String name, @RequestParam String password) {
        Optional<Queue> queueOptional = queueService.getQueueByName(name);

        if (queueOptional.isPresent()) {
            Queue queue = queueOptional.get();
            if (queue.getPassword().equals(password)) {
                queueService.closeQueue(queue);
            }
        }

        return "redirect:/queues";
    }


    @GetMapping("/getUserEntries")
    public String getUserEntries(@RequestParam String name, Model model) {
        List<QueueEntry> userEntries = queueService.getUserEntries(name);

        model.addAttribute("userName", name);
        model.addAttribute("userEntries", userEntries);

        return "user_info";
    }

    @GetMapping("/next")
    public String next(@RequestParam String name, @RequestParam String password) {
        Optional<Queue> queueOptional = queueService.getQueueByName(name);
        if (queueOptional.isPresent()) {
            Queue queue = queueOptional.get();
            if (queue.getPassword().equals(password)) {
                queueService.removeNextEntry(queue);
            }
        }

        return "redirect:/queue?name="+name;
    }
    @GetMapping("/removeUser")
    public String removeUser(@RequestParam String name, @RequestParam String userName, @RequestParam String password) {
        Optional<Queue> queueOptional = queueService.getQueueByName(name);
        if (queueOptional.isPresent()) {
            Queue queue = queueOptional.get();
            if (queue.getPassword().equals(password)) {
                queueService.removeQueueEntry(queue, userName);
            }
        }

        return "redirect:/queue?name="+name;
    }

}
