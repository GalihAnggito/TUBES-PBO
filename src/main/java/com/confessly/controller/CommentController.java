package com.confessly.controller;

import com.confessly.model.Komentar;
import com.confessly.model.User;
import com.confessly.service.KomentarService;
import com.confessly.service.AuthService;
import com.confessly.dto.LikeRequest;
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
        User user = authService.login(request.getUsername(), request.getPassword());
        if (user == null) {
            return ResponseEntity.badRequest().body("Please login first to like a comment");
        }

        if (!authService.isUserLoggedIn(user)) {
            return ResponseEntity.badRequest().body("Your session has expired. Please login again.");
        }

        try {
            int likes = komentarService.likeKomentar(id, user);
            Map<String, Object> response = new HashMap<>();
            response.put("likes", likes);
            response.put("liked", komentarService.getKomentar(id).hasLiked(user));
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
} 