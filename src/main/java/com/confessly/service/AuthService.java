package com.confessly.service;

import com.confessly.model.User;
import com.confessly.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    
    private Map<String, User> loggedInUsers = new ConcurrentHashMap<>();

    public User login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            loggedInUsers.put(username, user);
            return user;
        }
        return null;
    }

    public boolean isUserLoggedIn(User user) {
        return loggedInUsers.containsKey(user.getUsername());
    }

    public void logout(String username) {
        loggedInUsers.remove(username);
    }
} 