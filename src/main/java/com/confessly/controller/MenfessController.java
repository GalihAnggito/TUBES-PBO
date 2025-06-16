package com.confessly.controller;

import com.confessly.model.Menfes;
import com.confessly.model.User;
import com.confessly.model.Komentar;
import com.confessly.service.MenfesService;
import com.confessly.service.LoginLogout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/menfess")
public class MenfessController {

    @Autowired
    private MenfesService menfesService;

    @Autowired
    private LoginLogout authService;

    @GetMapping
    public List<Menfes> getAllMenfess() {
        return menfesService.lihatMenfesTerbaru();
    }

    @GetMapping("/popular")
    public List<Menfes> getPopularMenfess() {
        return menfesService.lihatMenfesPopuler();
    }

    @PostMapping
    public ResponseEntity<?> createMenfess(@RequestBody MenfesRequest request) {
        // Verify user is logged in
        User user = authService.login(request.getUsername(), request.getPassword());
        if (user == null) {
            return ResponseEntity.badRequest().body("Please login first to create a menfess post");
        }

        // Verify user is still logged in
        if (!authService.isUserLoggedIn(user)) {
            return ResponseEntity.badRequest().body("Your session has expired. Please login again.");
        }

        try {
            Menfes menfes = menfesService.buatMenfes(request.getContent(), user);
            return ResponseEntity.ok(menfes);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<?> likeMenfess(@PathVariable int id, @RequestBody LikeRequest request) {
        User user = authService.login(request.getUsername(), request.getPassword());
        if (user == null) {
            return ResponseEntity.badRequest().body("Please login first to like a menfess");
        }

        if (!authService.isUserLoggedIn(user)) {
            return ResponseEntity.badRequest().body("Your session has expired. Please login again.");
        }

        try {
            int likes = menfesService.likeMenfess(id, user);
            Map<String, Object> response = new HashMap<>();
            response.put("likes", likes);
            response.put("liked", menfesService.getMenfess(id).hasLiked(user));
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<?> addComment(@PathVariable int id, @RequestBody CommentRequest request) {
        User user = authService.login(request.getUsername(), request.getPassword());
        if (user == null) {
            return ResponseEntity.badRequest().body("Please login first to comment");
        }

        if (!authService.isUserLoggedIn(user)) {
            return ResponseEntity.badRequest().body("Your session has expired. Please login again.");
        }

        try {
            Komentar komentar = new Komentar();
            komentar.setIsi(request.getContent());
            komentar.setPengirim(user);
            boolean success = menfesService.komenMenfes(id, komentar);
            if (success) {
                Komentar savedKomentar = menfesService.getKomentarById(komentar.getId());
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
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteMenfess(@PathVariable int id, @RequestBody LoginRequest request) {
        User user = authService.login(request.getUsername(), request.getPassword());
        if (user == null) {
            return ResponseEntity.badRequest().body(false);
        }
        boolean success = menfesService.deleteMenfes(id, user);
        return ResponseEntity.ok(success);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editMenfess(@PathVariable int id, @RequestBody MenfesRequest request) {
        // Verify user is logged in
        User user = authService.login(request.getUsername(), request.getPassword());
        if (user == null) {
            return ResponseEntity.badRequest().body("Please login first to edit a menfess post");
        }

        // Verify user is still logged in
        if (!authService.isUserLoggedIn(user)) {
            return ResponseEntity.badRequest().body("Your session has expired. Please login again.");
        }

        try {
            Menfes menfes = menfesService.editMenfes(id, request.getContent(), user);
            return ResponseEntity.ok(menfes);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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

class LikeRequest {
    private String username;
    private String password;
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
} 