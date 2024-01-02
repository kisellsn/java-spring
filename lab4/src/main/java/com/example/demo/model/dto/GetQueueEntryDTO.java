package com.example.demo.model.dto;

import com.example.demo.model.Queue;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class GetQueueEntryDTO {
        private Long id;
        private Long userId;
        private String queue;

    public GetQueueEntryDTO() {
    }

    public GetQueueEntryDTO(Long id, Long userId, String queue) {
        this.id = id;
        this.userId = userId;
        this.queue = queue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }
}
