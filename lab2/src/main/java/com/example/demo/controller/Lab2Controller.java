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
import java.util.regex.Pattern;


@Controller
public class Lab2Controller {
    @Autowired
    private QueueService queueService;

    @Autowired
    private UserService userService;

    private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile("^[a-zA-Z0-9]+$");

    @PostMapping("/login")
    public String login(String userName, String userPassword) {
        User user = userService.getUserByName(userName);
        if (user == null) {
            return "error";
        }

        if (!user.getPassword().equals(userPassword)) {
            return "error";
        }

        return "redirect:getUserInfo?userId=" + user.getUserID();
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(String userName, String userPassword) {
        if (!isValidInput(userName) || !isValidInput(userPassword)) {
            return "error";
        }
        User user = userService.getUserByName(userName);
        if (user != null) {
            return "error";
        }
        this.userService.createUser(userName, userPassword);
        return "index";
    }

    @GetMapping("/queues")
    public String showQueues(@RequestParam int userId, Model model) {
        List<Queue> queues = queueService.getAllQueues();
        model.addAttribute("queues", queues);
        model.addAttribute("userId", userId);
        return "queues";
    }

    @GetMapping("/createQueue")
    public String showCreateQueueForm(@RequestParam int userId, Model model) {
        model.addAttribute("userId", userId);
        return "create_queue";
    }
    @PostMapping("/createQueue")
    public String createQueue(@RequestParam int userId, String queueName) {
        if (!isValidInput(queueName)) {
            return "error";
        }
        User user = userService.getUser(userId);
        if (user == null) {
            return "error";
        }
        queueService.createQueue(queueName, user.getLogin(), userId);
        return "redirect:/getUserInfo?userId=" + userId;
    }

    @PostMapping("/joinQueue")
    public String joinQueue(@RequestParam int queueID, int userId) {
        User user = userService.getUser(userId);
        if (user == null) {
            return "error";
        }
        Queue queue = this.queueService.getQueueByID(queueID);
        if (queue == null) {
            return "redirect:/queues?userId" + userId;
        }
        List<QueueEntry> entries = queueService.getQueueEntriesByQueue(queue);
        for (QueueEntry entry : entries) {
            if (entry.getUser().equals(user)) {
                return "redirect:/getUserInfo?userId=" + userId;
            }
        }
        queueService.joinQueue(queue, user);
        return "redirect:/getUserInfo?userId="+userId;
    }

    @GetMapping("/queue")
    public String showQueueDetails(@RequestParam int queueID, @RequestParam int userId, Model model) {
        Queue queue = queueService.getQueueByID(queueID);
        if (queue != null) {
            List<QueueEntry> entries = queueService.getQueueEntriesByQueue(queue);
            model.addAttribute("queue", queue);
            model.addAttribute("entries", entries);
            model.addAttribute("userId", userId);
            return "queue_details";
        } else {
            return "redirect:/queues?userId=" + userId;
        }
    }

    @GetMapping("/closeQueue")
    public String deleteQueue(@RequestParam int queueID, @RequestParam int userId) {
        User user = userService.getUser(userId);
        if (user == null) {
            return "error";
        }
        Queue queue = queueService.getQueueByID(queueID);
        if (queue != null) {
            queueService.closeQueue(queue);
        }

        return "redirect:/getUserInfo?userId=" + userId;
    }


    @GetMapping("/getUserInfo")
    public String getUserEntries(@RequestParam int userId, Model model) {
        User user = this.userService.getUser(userId);
        if (user == null) {
            return "error";
        }

        String userName = user.getLogin();

        List<QueueEntry> userEntries = queueService.getUserEntries(userName);
        List<Queue> userQueues = queueService.getUserQueues(user.getUserID());

        model.addAttribute("userId", userId);
        model.addAttribute("userName", userName);
        model.addAttribute("userEntries", userEntries);
        model.addAttribute("userQueues", userQueues);
        return "user_info";
    }

    @GetMapping("/next")
    public String next(@RequestParam int queueID, @RequestParam int userId) {
        Queue queue = queueService.getQueueByID(queueID);
        if (queue != null) {
            queueService.removeNextEntry(queue);
        }
        return "redirect:/getUserInfo?userId=" + userId;
    }
    @GetMapping("/removeUser")
    public String removeUser(@RequestParam int queueID, int currentUserId, String userName) {
        User user = this.userService.getUserByName(userName);
        if (user == null) {
            return "error";
        }
        Queue queue = queueService.getQueueByID(queueID);
        if (queue != null) {
            queueService.removeQueueEntry(queue, user);
        }

        return "redirect:/queue?queueID=" + queueID+ "&userId=" + currentUserId;
    }
    private boolean isValidInput(String input) {
        return input != null && ALPHANUMERIC_PATTERN.matcher(input).matches();
    }

    @GetMapping("/lockQueue")
    public String lockQueue(@RequestParam int queueID, int userId, boolean isLocked) {

        Queue queue = queueService.getQueueByID(queueID);
        if (queue != null) {
            queueService.setLocked(queue, isLocked);
        }

        return "redirect:/getUserInfo?userId=" + userId;
    }

}

