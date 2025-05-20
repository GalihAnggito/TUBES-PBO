package com.confessly.service;

import com.confessly.model.User;

public class LoginLogout {
    public User login(String username, String password) {
        // In a real application, this would validate against a database
        if ("demo".equals(username) && "password".equals(password)) {
            return new User(1, username, password, "user");
        }
        return null;
    }

    public boolean logout(User user) {
        if (user != null) {
            // In a real application, this would handle session cleanup
            return true;
        }
        return false;
    }
} 