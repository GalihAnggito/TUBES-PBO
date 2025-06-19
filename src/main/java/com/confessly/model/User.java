package com.confessly.model;

import javax.persistence.*;


@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    private String role;

    public User() {
        this.role = Role.USER_ROLE;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.role = Role.USER_ROLE;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return new Role(this.role);
    }

    public void setRole(Role role) {
        this.role = role.getRoleName();
    }
} 