package com.example.demo.controller;
import com.example.demo.model.Queue;
import com.example.demo.model.QueueEntry;
import com.example.demo.model.User;
import com.example.demo.service.QueueService;
import com.example.demo.service.UserService;
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
    @Autowired
    private QueueService queueService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public String login(String userName, String userPassword, Model model) {
        User user = userService.getUserByName(userName);
        if (user == null) {
            return "error";
        }

        if (!user.getPassword().equals(userPassword)) {
            return "error";
        }

        return "redirect:getUserInfo?userId=" + user.getId();
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(String userName, String userPassword) {
        User user = userService.getUserByName(userName);
        if (user != null) {
            return "error";
        }
        this.userService.createUser(userName, userPassword);
        return "index";
    }

    @GetMapping("/queues")
    public String showQueues(Long userId, Model model) {
        List<Queue> queues = queueService.getAllQueues();
        model.addAttribute("queues", queues);
        model.addAttribute("userId", userId);
        return "queues";
    }

    @GetMapping("/createQueue")
    public String showCreateQueueForm(@RequestParam Long userId, Model model) {
        model.addAttribute("userId", userId);
        return "create_queue";
    }
    @PostMapping("/createQueue")
    public String createQueue(@RequestParam Long userId, String queueName) {
        User user = userService.getUser(userId);
        if (user == null) {
            return "error";
        }
        Queue newQueue = queueService.createQueue(queueName, user.getName(), userId);
        return "redirect:/getUserInfo?userId=" + userId;
    }

    @PostMapping("/joinQueue")
    public String joinQueue(@RequestParam String name, Long userId) {
        User user = userService.getUser(userId);
        if (user == null) {
            return "error";
        }
        Queue queue = this.queueService.getQueueByName(name).orElse(null);
        if (queue == null) {
            return "error";
        }
        List<QueueEntry> entries = queueService.getQueueEntriesByQueue(queue);
        for (QueueEntry entry : entries) {
            if (entry.getUserName().equals(user.getName())) {
                return "redirect:/getUserInfo?userId=" + userId;
            }
        }
        queueService.joinQueue(queue, user.getName());
        return "redirect:/getUserInfo?userId="+userId;
    }

    @GetMapping("/queue")
    public String showQueueDetails(@RequestParam String name, @RequestParam Long userId, Model model) {
        Optional<Queue> queue = queueService.getQueueByName(name);
        if (queue.isPresent()) {
            List<QueueEntry> entries = queueService.getQueueEntriesByQueue(queue.get());
            model.addAttribute("queue", queue.get());
            model.addAttribute("entries", entries);
            model.addAttribute("userId", userId);
            return "queue_details";
        } else {
            return "error";
//            return "redirect:/queues";
        }
    }

    @GetMapping("/closeQueue")
    public String deleteQueue(@RequestParam String name, @RequestParam Long userId) {
        User user = userService.getUser(userId);
        if (user == null) {
            return "error";
        }
        Optional<Queue> queueOptional = queueService.getQueueByName(name);

        if (queueOptional.isPresent()) {
            Queue queue = queueOptional.get();
            queueService.closeQueue(queue);
        }

        return "redirect:/getUserInfo?userId=" + userId;
    }


    @GetMapping("/getUserInfo")
    public String getUserEntries(@RequestParam Long userId, Model model) {
        User user = this.userService.getUser(userId);
        if (user == null) {
            return "error";
        }

        String userName = user.getName();

        List<QueueEntry> userEntries = queueService.getUserEntries(userName);
        List<Queue> userQueues = queueService.getUserQueues(user.getName());

        model.addAttribute("userId", userId);
        model.addAttribute("userName", userName);
        model.addAttribute("userEntries", userEntries);
        model.addAttribute("userQueues", userQueues);
        return "user_info";
    }

    @GetMapping("/next")
    public String next(@RequestParam String name, @RequestParam Long userId) {
        Optional<Queue> queueOptional = queueService.getQueueByName(name);
        if (queueOptional.isPresent()) {
            Queue queue = queueOptional.get();
            queueService.removeNextEntry(queue);
        }
        return "redirect:/getUserInfo?userId=" + userId;
    }
    @GetMapping("/removeUser")
    public String removeUser(@RequestParam String name, Long userId) {
        User user = this.userService.getUser(userId);
        if (user == null) {
            return "error";
        }
        Optional<Queue> queueOptional = queueService.getQueueByName(name);
        if (queueOptional.isPresent()) {
            Queue queue = queueOptional.get();
            queueService.removeQueueEntry(queue, user.getName());
        }

        return "redirect:/queue?userId=" + userId + "&name=" + name;
    }

}
