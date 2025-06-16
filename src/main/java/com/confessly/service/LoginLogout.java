package com.confessly.service;

import com.confessly.model.User;
import com.confessly.model.Role;
import com.confessly.repository.UserRepository;
import com.confessly.exception.ResourceNotFoundException;
import com.confessly.exception.UsernameAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginLogout {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AuthService authService;

    public User login(String username, String password) {
        return authService.login(username, password);
    }

    public boolean isUserLoggedIn(User user) {
        return authService.isUserLoggedIn(user);
    }

    public void logout(String username) {
        authService.logout(username);
    }

    public User register(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException("Username '" + username + "' already exists");
        }

        User user = new User(username, password);
        if ("admin1".equals(username) && "admin123".equals(password)) {
            user.setRole(new Role(Role.ADMIN_ROLE));
        }
        return userRepository.save(user);
    }

    public User getUserById(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
    }

    public boolean isUsernameTaken(String username) {
        return userRepository.existsByUsername(username);
    }
} 