package com.confessly.service;

import com.confessly.model.User;
import com.confessly.repository.UserRepository;
import com.confessly.exception.ResourceNotFoundException;
import com.confessly.exception.UsernameAlreadyExistsException;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
    private final LoginLogout loginLogout;
    private final UserRepository userRepository;

    public ProfileService(LoginLogout loginLogout, UserRepository userRepository) {
        this.loginLogout = loginLogout;
        this.userRepository = userRepository;
    }

    public User updateUsername(int userId, String newUsername) {
        User user = loginLogout.getUserById(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with id " + userId);
        }
        
        // Check if username is already taken
        if (userRepository.existsByUsername(newUsername)) {
            throw new UsernameAlreadyExistsException("Username '" + newUsername + "' already exists");
        }
        user.setUsername(newUsername);
        return userRepository.save(user);
    }
} 