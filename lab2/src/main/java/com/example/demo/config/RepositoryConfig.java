package com.example.demo.config;

import com.example.demo.repositories.QueueRepository;
import com.example.demo.repositories.QueueRepositoryFake;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {
    @Bean
    public QueueRepository queueRepositoryFake(){
        return new QueueRepositoryFake();
    }
}
