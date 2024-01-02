package com.example.jdbcdemo.queue;

import com.example.jdbcdemo.user.UserEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "`Queue`")
public class QueueEntity {

    @Id
    @Column(name = "queueID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int queueID;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "isLocked", nullable = false)
    private boolean isLocked;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "ownerID", referencedColumnName = "userID")
    private UserEntity owner;

    @ManyToMany(cascade = { CascadeType.REMOVE })
    @JoinTable(
            name = "PlaceInQueue",
            joinColumns = @JoinColumn(name = "queueID"),
            inverseJoinColumns = @JoinColumn(name = "userID")
    )
    @OrderColumn(name = "recordID")
    private List<UserEntity> users;

    public QueueEntity() {}

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

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public void addUser(UserEntity user) {
        users.add(user);
    }

    public void deleteUser(UserEntity user) {
        users.remove(user);
    }

    public List<UserEntity> getUsers() {
        return users;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }
}
