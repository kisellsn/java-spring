package com.example.lab1web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Lab1WebController {

    @GetMapping("/HelloWord")
    @ResponseBody
    String HelloWord(){
        return "Hello, Word!";
    }

    @GetMapping("/")
    String index(){
        return "index";
    }


    @GetMapping("/1")
    String first(){
        return "1";
    }
    @GetMapping("/2")
    String second(){
        return "2";
    }
    @GetMapping("/3")
    String third(){
        return "3";
    }
    @GetMapping("/4")
    String fourth(){
        return "4";
    }


}