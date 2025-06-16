package com.confessly.service;

import com.confessly.model.User;
import com.confessly.repository.UserRepository;
import com.confessly.exception.ResourceNotFoundException;
import com.confessly.exception.UsernameAlreadyExistsException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ProfileService {
    private final LoginLogout loginLogout;
    private final UserRepository userRepository;
    private final String UPLOAD_DIR = "uploads/profile-pictures/";

    public ProfileService(LoginLogout loginLogout, UserRepository userRepository) {
        this.loginLogout = loginLogout;
        this.userRepository = userRepository;
        // Create upload directory if it doesn't exist
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public User updateProfilePicture(int userId, MultipartFile file) {
        User user = loginLogout.getUserById(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with id " + userId);
        }
        
        if (file != null && !file.isEmpty()) {
            try {
                // Generate unique filename
                String filename = UUID.randomUUID().toString() + getFileExtension(file.getOriginalFilename());
                Path filepath = Paths.get(UPLOAD_DIR + filename);
                
                // Save file
                Files.copy(file.getInputStream(), filepath);
                
                // Delete old profile picture if it exists and is not the default
                if (!user.getProfilePicture().equals("default.png")) {
                    Path oldFile = Paths.get(UPLOAD_DIR + user.getProfilePicture());
                    Files.deleteIfExists(oldFile);
                }
                
                // Update user's profile picture
                user.setProfilePicture(filename);
                return userRepository.save(user);
            } catch (IOException e) {
                throw new RuntimeException("Failed to store profile picture", e);
            }
        }
        return user;
    }

    private String getFileExtension(String filename) {
        if (filename == null) return "";
        int lastDotIndex = filename.lastIndexOf(".");
        return lastDotIndex == -1 ? "" : filename.substring(lastDotIndex);
    }
} 