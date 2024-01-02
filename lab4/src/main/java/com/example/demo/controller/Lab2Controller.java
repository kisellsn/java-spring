package com.example.demo.controller;
import com.example.demo.model.Queue;
import com.example.demo.model.User;
import com.example.demo.service.IQueueService;
import com.example.demo.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;


@Controller
public class Lab2Controller {
    @Autowired
    private IQueueService queueService;

    @Autowired
    private IUserService userService;

    @PostMapping("/login")
    public String login(String userName, String userPassword) {
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
        if(!this.userService.createUser(userName, userPassword)){
            return "error";
        }
        return "index";
    }

    @GetMapping("/queues")
    public String showQueues(@RequestParam Long userId, Model model) {
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
    public String createQueue(@RequestParam String userName, String queueName) {
        queueService.createQueue(queueName, userName);
        return "redirect:/getUserInfo?userId=" + userName;
    }

    @PostMapping("/joinQueue")
    public String joinQueue(@RequestParam String name, Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return "error";
        }
        Queue queue = this.queueService.getQueueByName(name);
        if (queue == null) {
            return "redirect:/queues?userId" + userId;
        }
        List<User> entries = queueService.getQueueEntriesByQueue(queue);
        for (User entry : entries) {
            if (entry.getName().equals(user.getName())) {
                return "redirect:/getUserInfo?userId=" + userId;
            }
        }
        queueService.joinQueue(queue, user.getName());
        return "redirect:/getUserInfo?userId="+userId;
    }

    @GetMapping("/queue")
    public String showQueueDetails(@RequestParam String name, @RequestParam Long userId, Model model) {
        Queue queue = queueService.getQueueByName(name);
        if (queue != null) {
            List<User> entries = queueService.getQueueEntriesByQueue(queue);
            model.addAttribute("queue", queue);
            model.addAttribute("entries", entries);
            model.addAttribute("userId", userId);
            return "queue_details";
        } else {
            return "redirect:/queues?userId=" + userId;
        }
    }

    @GetMapping("/closeQueue")
    public String deleteQueue(@RequestParam String name, @RequestParam Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return "error";
        }
        Queue queue = queueService.getQueueByName(name);
        if (queue != null) {
            queueService.closeQueue(queue);
        }

        return "redirect:/getUserInfo?userId=" + userId;
    }


    @GetMapping("/getUserInfo")
    public String getUserEntries(@RequestParam Long userId, Model model) {
        User user = this.userService.getUserById(userId);
        if (user == null) {
            return "error";
        }

        String userName = user.getName();

        List<User> userEntries = queueService.getUserEntries(userName);
        List<Queue> userQueues = queueService.getUserQueues(user.getId());

        model.addAttribute("userId", userId);
        model.addAttribute("userName", userName);
        model.addAttribute("userEntries", userEntries);
        model.addAttribute("userQueues", userQueues);
        return "user_info";
    }

    @GetMapping("/next")
    public String next(@RequestParam String name, @RequestParam Long userId) {
        Queue queue = queueService.getQueueByName(name);
        if (queue != null) {
            queueService.removeNextEntry(queue);
        }
        return "redirect:/getUserInfo?userId=" + userId;
    }
    @GetMapping("/removeUser")
    public String removeUser(@RequestParam String name, Long currentUserId, String userName) {
        User user = this.userService.getUserByName(userName);
        if (user == null) {
            return "error";
        }
        Queue queue = queueService.getQueueByName(name);
        if (queue != null) {
            queueService.removeQueueEntry(queue, userName);
        }

        return "redirect:/queue?userId=" + currentUserId + "&name=" + name;
    }
}

