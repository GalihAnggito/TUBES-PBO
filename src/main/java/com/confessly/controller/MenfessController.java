package com.confessly.controller;

import com.confessly.model.Menfes;
import com.confessly.model.User;
import com.confessly.model.Komentar;
import com.confessly.service.MenfesService;
import com.confessly.service.AuthService;
import com.confessly.dto.LikeRequest;
import com.confessly.dto.LoginRequest;
import com.confessly.exception.BadCredentialsException;
import com.confessly.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/menfess")
public class MenfessController {

    private static final Logger logger = LoggerFactory.getLogger(MenfessController.class);

    @Autowired
    private MenfesService menfesService;

    @Autowired
    private AuthService authService;

    @GetMapping
    public ResponseEntity<?> getAllMenfess() {
        return ResponseEntity.ok(menfesService.lihatMenfesTerbaru());
    }

    @GetMapping("/popular")
    public ResponseEntity<?> getPopularMenfess() {
        return ResponseEntity.ok(menfesService.lihatMenfesPopuler());
    }

    @PostMapping
    public ResponseEntity<?> createMenfess(@RequestBody MenfesRequest request) {
        try {
            // Verify user is logged in
            User user = authService.login(request.getUsername(), request.getPassword());
            if (!authService.isUserLoggedIn(user)) {
                return ResponseEntity.status(401).body("Your session has expired. Please login again.");
            }
            Menfes menfes = menfesService.buatMenfes(request.getContent(), user);
            return ResponseEntity.ok(menfes);
        } catch (BadCredentialsException e) { 
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (IllegalStateException e) { 
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) { 
            return ResponseEntity.status(500).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<?> likeMenfess(@PathVariable int id, @RequestBody LikeRequest request) {
        try {
            User user = authService.login(request.getUsername(), request.getPassword());
            if (!authService.isUserLoggedIn(user)) {
                return ResponseEntity.status(401).body("Your session has expired. Please login again.");
            }
            int likes = menfesService.likeMenfess(id, user);
            Map<String, Object> response = new HashMap<>();
            response.put("likes", likes);
            response.put("liked", menfesService.getMenfess(id).hasLiked(user));
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<?> addComment(@PathVariable int id, @RequestBody CommentRequest request) {
        try {
            User user = authService.login(request.getUsername(), request.getPassword());
            if (user == null) {
                return ResponseEntity.status(401).body("Please login first to comment");
            }
            if (!authService.isUserLoggedIn(user)) {
                return ResponseEntity.status(401).body("Your session has expired. Please login again.");
            }
            Komentar komentar = new Komentar();
            komentar.setIsi(request.getContent());
            komentar.setPengirim(user);
            boolean success = menfesService.komenMenfes(id, komentar);
            if (success) {
                Komentar savedKomentar = menfesService.getKomentarById(komentar.getID());
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("comment", savedKomentar);
                response.put("user", Map.of(
                    "id", user.getId(),
                    "username", user.getUsername(),
                    "profilePicture", user.getProfilePicture()
                ));
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.badRequest().body("Failed to add comment");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteMenfess(@PathVariable int id, @RequestBody LoginRequest request) {
        try {
            User user = authService.login(request.getUsername(), request.getPassword());
            if (user == null) {
                logger.warn("Attempt to delete menfess without login for username: {}", request.getUsername());
                return ResponseEntity.status(401).body(false);
            }
            logger.info("User {} (Role: {}) attempting to delete menfess with ID {}", user.getUsername(), user.getRole().getRoleName(), id);
            boolean success = menfesService.deleteMenfes(id, user);
            return ResponseEntity.ok(success);
        } catch (ResourceNotFoundException e) {
            logger.error("Menfess not found for deletion: {}", e.getMessage());
            return ResponseEntity.status(404).body(false);
        } catch (BadCredentialsException e) {
            logger.warn("Invalid credentials for menfess deletion: {}", e.getMessage());
            return ResponseEntity.status(401).body(false);
        } catch (Exception e) {
            logger.error("An unexpected error occurred during menfess deletion: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(false);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editMenfess(@PathVariable int id, @RequestBody MenfesRequest request) {
        try {
            // Verify user is logged in
            User user = authService.login(request.getUsername(), request.getPassword());
            if (user == null) {
                logger.warn("Attempt to edit menfess without login for username: {}", request.getUsername());
                return ResponseEntity.status(401).body("Please login first to edit a menfess post");
            }

            // Verify user is still logged in
            if (!authService.isUserLoggedIn(user)) {
                logger.warn("Session expired for user {} attempting to edit menfess", user.getUsername());
                return ResponseEntity.status(401).body("Your session has expired. Please login again.");
            }
            logger.info("User {} (Role: {}) attempting to edit menfess with ID {}", user.getUsername(), user.getRole().getRoleName(), id);
            Menfes menfes = menfesService.editMenfes(id, request.getContent(), user);
            return ResponseEntity.ok(menfes);
        } catch (ResourceNotFoundException e) {
            logger.error("Menfess not found for editing: {}", e.getMessage());
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (BadCredentialsException e) {
            logger.warn("Invalid credentials for menfess editing: {}", e.getMessage());
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (IllegalStateException e) {
            logger.warn("Permission denied for user {} attempting to edit menfess {}: {}", request.getUsername(), id, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("An unexpected error occurred during menfess editing: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/trending")
    public ResponseEntity<List<String>> getTrendingHashtags() {
        List<String> trendingHashtags = menfesService.getTrendingHashtags();
        return ResponseEntity.ok(trendingHashtags);
    }

    @GetMapping("/my")
    public List<Menfes> getMyMenfess(@RequestParam String username) {
        return menfesService.lihatMenfesSaya(username);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserMenfess(@PathVariable int userId) {
        return ResponseEntity.ok(menfesService.getMenfessByUserId(userId));
    }
}

class MenfesRequest {
    private String username;
    private String content;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

class CommentRequest {
    private String username;
    private String content;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
} 