package com.confessly.controller;

import com.confessly.model.Komentar;
import com.confessly.model.User;
import com.confessly.service.KomentarService;
import com.confessly.service.AuthService;
import com.confessly.dto.LikeRequest;
import com.confessly.dto.LoginRequest;
import com.confessly.exception.ResourceNotFoundException;
import com.confessly.exception.BadCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    @Autowired
    private KomentarService komentarService;
    
    @Autowired
    private AuthService authService;

    @PostMapping("/{id}/like")
    public ResponseEntity<?> likeComment(@PathVariable int id, @RequestBody LikeRequest request) {
        try {
            User user = authService.login(request.getUsername(), request.getPassword());
            if (!authService.isUserLoggedIn(user)) {
                return ResponseEntity.status(401).body("Your session has expired. Please login again.");
            }
            int likes = komentarService.likeKomentar(id, user);
            Map<String, Object> response = new HashMap<>();
            response.put("likes", likes);
            response.put("liked", komentarService.getKomentar(id).hasLiked(user));
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable int id, @RequestBody LoginRequest request) {
        try {
            User user = authService.login(request.getUsername(), request.getPassword());
            if (!authService.isUserLoggedIn(user)) {
                return ResponseEntity.status(401).body("Your session has expired. Please login again.");
            }
            boolean success = komentarService.deleteKomentar(id, user);
            if (success) {
                return ResponseEntity.ok().body("Comment deleted successfully");
            }
            return ResponseEntity.status(403).body("You don't have permission to delete this comment or comment not found.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred: " + e.getMessage());
        }
    }
} 