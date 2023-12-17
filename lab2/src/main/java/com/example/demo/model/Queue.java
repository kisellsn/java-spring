package com.example.demo.model;


import java.util.ArrayList;
import java.util.List;

public class Queue {
    private int queueID;
    private String name;
    private String code;
    private int ownerID;
    private boolean isLocked;
    private final List<QueueEntry> queueEntries = new ArrayList<>();

    public void addEntry(QueueEntry entry) {
        entry.setId(queueEntries.size() + 1);
        queueEntries.add(entry);
    }
    public void removeEntry() {
        QueueEntry removedEntry = queueEntries.remove(0);
        for (QueueEntry currentEntry : queueEntries) {
            currentEntry.setId(currentEntry.getId() - 1);
        }
    }

    public Queue(String name, String code, int ownerID) {
        this.name = name;
        this.code = code;
        this.ownerID = ownerID;
        this.isLocked = false;
    }

    public Queue() {
    }

    public int getQueueID() {
        return queueID;
    }

    public void setQueueID(int queueID) {
        this.queueID = queueID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public List<QueueEntry> getQueueEntries() {
        return queueEntries;
    }
}

