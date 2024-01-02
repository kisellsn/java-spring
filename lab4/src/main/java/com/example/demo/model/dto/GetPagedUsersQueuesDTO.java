package com.example.demo.model.dto;

import org.springframework.web.bind.annotation.RequestParam;

public class GetPagedUsersQueuesDTO {
    private Long ownerId;
    private int page;
    private int size;

    public GetPagedUsersQueuesDTO() {
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
