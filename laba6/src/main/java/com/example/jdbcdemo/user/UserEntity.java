package com.example.jdbcdemo.user;

import com.example.jdbcdemo.queue.QueueEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Set;

@Entity
@DynamicUpdate
@NamedQuery(
        name = "UserEntity.findAllOwners",
        query = "SELECT DISTINCT u FROM UserEntity u, QueueEntity q WHERE u.userID = q.owner.userID"
)
@Table(name = "`User`")
public class UserEntity {

    @Id
    @Column(name = "userID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userID;

    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "owner")
    private Set<QueueEntity> ownedQueues;

    @ManyToMany(mappedBy = "users")
    private Set<QueueEntity> queues;

    public UserEntity() {}

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<QueueEntity> getQueues() {
        return queues;
    }

    public Set<QueueEntity> getOwnedQueues() {
        return ownedQueues;
    }
}
