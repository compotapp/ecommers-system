package com.ecommers.serviceuser.entity;

import com.ecommers.serviceuser.role.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class UserRole {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", updatable = false)
    private User user;

    @Enumerated(STRING)
    @Column(updatable = false)
    private RoleType type;

    public UserRole() {
    }

    public UserRole(User user, RoleType type) {
        this.user = user;
        this.type = type;
    }

    public UserRole(Long id, User user, RoleType type) {
        this.id = id;
        this.user = user;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RoleType getType() {
        return type;
    }

    public void setType(RoleType type) {
        this.type = type;
    }
}
