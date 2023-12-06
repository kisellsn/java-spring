package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Lab2Application {

    public static void main(String[] args) {
        SpringApplication.run(Lab2Application.class, args);
    }

//    @Bean
//    QueueService queueService(QueueRepositoryStub queueRepository){
//        return new QueueService(queueRepository);
//    }
//    @Bean
//    QueueRepositoryStub queueRepository(){
//        return new QueueRepositoryStub();
//    }

}

