package com.confessly.controller;

import com.confessly.model.User;
import com.confessly.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PutMapping("/username")
    public ResponseEntity<?> updateUsername(@RequestParam int userId, @RequestParam String newUsername) {
        User updatedUser = profileService.updateUsername(userId, newUsername);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.badRequest().body("Username is already taken or user not found");
    }
} 