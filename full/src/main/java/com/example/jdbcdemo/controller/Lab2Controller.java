package com.example.jdbcdemo.controller;
import com.example.jdbcdemo.models.Queue;
import com.example.jdbcdemo.models.User;

import com.example.jdbcdemo.service.QueueService;
import com.example.jdbcdemo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@CrossOrigin(origins = "http://localhost:8080")
@Controller
public class Lab2Controller {
    @Autowired
    private QueueService queueService;

    @Autowired
    private UserService userService;

    private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile("^[a-zA-Z0-9]+$");

    @PostMapping("/login")
    public String login(String userLogin, String password) {
        User user = userService.getUserByName(userLogin);
        if (user == null) {
            return "error";
        }

        if (!user.getPassword().equals(password)) {
            return "error";
        }

        return "redirect:getUserInfo?userId=" + user.getUserID();
    }
    @GetMapping("/")
    public String startPage() {
        return "index";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String userLogin, String password) {
        if (!isValidInput(userLogin) || !isValidInput(password)) {return "error";}
        User user = userService.getUserByName(userLogin);
        if (user != null) {return "error";}

        userService.add(userLogin, password);
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
    public String createQueue(@RequestParam int userId, String queueName, String queueCode, boolean isLocked) {
        if (!isValidInput(queueName)) {return "error";}
        User user = userService.getUser(userId);
        if (user == null) {return "error";}

        queueService.add(queueName, queueCode, isLocked, userId);
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
        List<User> entries = queueService.getAllUsersInQueue(queueID);
        for (User entry : entries) {
            if (entry.equals(user)) {
                return "redirect:/getUserInfo?userId=" + userId;
            }
        }
        queueService.joinQueue(queueID, userId);
        return "redirect:/getUserInfo?userId="+userId;
    }

    @GetMapping("/queue")
    public String showQueueDetails(@RequestParam int queueID, @RequestParam int userId, Model model) {
        Queue queue = queueService.getQueueByID(queueID);
        if (queue != null) {
            List<User> users = queueService.getAllUsersInQueue(queueID); //////// отримати місце
            model.addAttribute("queue", queue);
            model.addAttribute("entries", users);
            model.addAttribute("userId", userId);
            return "queue_details";
        } else {
            return "redirect:/queues?userId=" + userId;
        }
    }

    @GetMapping("/closeQueue")
    public String deleteQueue(@RequestParam int queueID, @RequestParam int userId, @RequestParam String code) {
        User user = userService.getUser(userId);
        if (user == null) {
            return "error";
        }
        queueService.closeQueue(queueID, code);

        return "redirect:/getUserInfo?userId=" + userId;
    }


    @GetMapping("/getUserInfo")
    public String getUserEntries(@RequestParam int userId, Model model) {
        User user = this.userService.getUser(userId);
        if (user == null) {
            return "error";
        }

        List<Map<String, Object>> userEntries = queueService.getUserEntries(userId); //////////////////
        List<Queue> userQueues = queueService.getUserQueues(userId);


        model.addAttribute("userId", userId);
        model.addAttribute("userName", user.getLogin());
        model.addAttribute("userEntries", userEntries);
        model.addAttribute("userQueues", userQueues);
        model.addAttribute("queueService", queueService);
        return "user_info";
    }

    @GetMapping("/next")
    public String next(@RequestParam int queueID, @RequestParam int userId, @RequestParam String code) {
        queueService.removeNextEntry(queueID, code);
        return "redirect:/getUserInfo?userId=" + userId;
    }
    @GetMapping("/removeUser")
    public String removeUser(@RequestParam int queueID, int currentUserId, String userLogin, String code) {
        User user = userService.getUserByName(userLogin);
        if (user == null) {
            return "error";
        }

        queueService.removeQueueEntry(queueID, currentUserId, code);

        return "redirect:/queue?queueID=" + queueID+ "&userId=" + currentUserId;
    }
    @GetMapping("/lockQueue")
    public String lockQueue(@RequestParam int queueID, int userId, boolean isLocked) {
        queueService.setLocked(queueID, isLocked);
        return "redirect:/getUserInfo?userId=" + userId;
    }


    private boolean isValidInput(String input) {
        return input != null && ALPHANUMERIC_PATTERN.matcher(input).matches();
    }


}

