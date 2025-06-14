package com.confessly.service;

import com.confessly.model.User;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoginLogout {
    private static List<User> users = new ArrayList<>();
    private static int nextUserId = 1;

    static {
        // Tambahkan user default admin
        users.add(new User(nextUserId++, "admin", "123", "admin"));
        // Tambahkan user default user
        users.add(new User(nextUserId++, "user", "123", "user"));
    }

    public User login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
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

    public User register(String username, String password) {
        // Check if username already exists
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return null;
            }
        }

        // Create new user
        User newUser = new User(nextUserId++, username, password, "user");
        users.add(newUser);
        return newUser;
    }

    public boolean isUserLoggedIn(User user) {
        if (user == null) return false;
        for (User u : users) {
            if (u.getId() == user.getId() && u.getUsername().equals(user.getUsername())) {
                return true;
            }
        }
        return false;
    }

    public User getUserById(int userId) {
        for (User user : users) {
            if (user.getId() == userId) {
                return user;
            }
        }
        return null;
    }

    public boolean isUsernameTaken(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
} 