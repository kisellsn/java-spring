package com.example.demo.config;

import com.example.demo.model.Queue;
import com.example.demo.repository.QueueRepositoryFake;
import com.example.demo.repository.RepositoryInterface;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {
    @Bean
    public RepositoryInterface<Queue> queueRepositoryFake(){
        return new QueueRepositoryFake();
    }
}
