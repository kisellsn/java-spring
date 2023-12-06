package com.example.demo.model;


import java.util.ArrayList;
import java.util.List;

public class Queue {
    private Long id;
    private String name;
    private String ownerName;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private final List<QueueEntry> queueEntries = new ArrayList<>();

    public void addEntry(QueueEntry entry) {
        entry.setId((long) (queueEntries.size() + 1));
        queueEntries.add(entry);
    }
    public QueueEntry removeEntry() {
        QueueEntry removedEntry = queueEntries.remove(0);
        for (QueueEntry currentEntry : queueEntries) {
            currentEntry.setId(currentEntry.getId() - 1);
        }
        return removedEntry;
    }
    public void removeEntry(QueueEntry entry) {
        long removedId = entry.getId();
        queueEntries.remove(entry);
        for (QueueEntry currentEntry :  queueEntries.subList((int) (removedId-1), queueEntries.size())) {
            currentEntry.setId(currentEntry.getId() - 1);
        }
    }
    public List<QueueEntry> getQueueEntries(){
        return queueEntries;
    }


    public Queue( String name, String ownerName, String password) {
        this.name = name;
        this.ownerName = ownerName;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}

