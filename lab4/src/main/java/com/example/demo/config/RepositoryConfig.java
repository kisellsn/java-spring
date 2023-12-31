package com.example.demo.config;

import com.example.demo.model.Queue;
import com.example.demo.repositories.QueueRepositoryFake;
import com.example.demo.repositories.IRepositoryInterface;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {
    @Bean
    public IRepositoryInterface<Queue> queueRepositoryFake(){
        return new QueueRepositoryFake();
    }
}
