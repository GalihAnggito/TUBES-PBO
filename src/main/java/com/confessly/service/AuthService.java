package com.confessly.service;

import com.confessly.model.User;
import com.confessly.repository.UserRepository;
import com.confessly.exception.BadCredentialsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;
    
    private Map<String, User> loggedInUsers = new ConcurrentHashMap<>();

    public User login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            logger.warn("Login attempt failed for username: {}", username);
            throw new BadCredentialsException("Invalid username or password");
        }
        logger.info("User {} logged in successfully with role: {}", user.getUsername(), user.getRole().getRoleName());
        loggedInUsers.put(username, user);
        return user;
    }

    public boolean isUserLoggedIn(User user) {
        return loggedInUsers.containsKey(user.getUsername());
    }

    public void logout(String username) {
        logger.info("User {} logged out.", username);
        loggedInUsers.remove(username);
    }
} 