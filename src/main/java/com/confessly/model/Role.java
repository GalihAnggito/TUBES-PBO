package com.confessly.model;

public class Role {
    private String admin;
    private String user;

    public Role() {
        this.admin = "admin";
        this.user = "user";
    }

    public String getRoleAdmin() {
        return admin;
    }

    public String getRoleUser() {
        return user;
    }

    public boolean validRole(String role) {
        return role.equals(admin) || role.equals(user);
    }
} 