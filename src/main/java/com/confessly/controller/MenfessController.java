package com.confessly.controller;

import com.confessly.model.Menfes;
import com.confessly.model.User;
import com.confessly.model.Komentar;
import com.confessly.service.MenfesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menfess")
public class MenfessController {

    @Autowired
    private MenfesService menfesService;

    @GetMapping
    public List<Menfes> getAllMenfess() {
        return menfesService.lihatMenfesTerbaru();
    }

    @GetMapping("/popular")
    public List<Menfes> getPopularMenfess() {
        return menfesService.lihatMenfesPopuler();
    }

    @PostMapping
    public ResponseEntity<Menfes> createMenfess(@RequestBody MenfesRequest request) {
        User user = new User(1, request.getUsername(), "", "user"); // In real app, get from session
        Menfes menfes = menfesService.buatMenfes(request.getContent(), user);
        return ResponseEntity.ok(menfes);
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Integer> likeMenfess(@PathVariable int id) {
        int likes = menfesService.likeMenfes(id);
        return ResponseEntity.ok(likes);
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<Boolean> addComment(@PathVariable int id, @RequestBody CommentRequest request) {
        User user = new User(1, request.getUsername(), "", "user"); // In real app, get from session
        Komentar komentar = new Komentar(
            (int) (Math.random() * 1000),
            request.getContent(),
            user,
            null // Will be set by service
        );
        boolean success = menfesService.komenMenfes(id, komentar);
        return ResponseEntity.ok(success);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteMenfess(@PathVariable int id) {
        User user = new User(1, "admin", "", "admin"); // In real app, get from session
        boolean success = menfesService.deleteMenfes(id, user);
        return ResponseEntity.ok(success);
    }

    @GetMapping("/trending")
    public ResponseEntity<List<String>> getTrendingHashtags() {
        List<String> trendingHashtags = menfesService.getTrendingHashtags();
        return ResponseEntity.ok(trendingHashtags);
    }
}

class MenfesRequest {
    private String username;
    private String content;

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
}

class CommentRequest {
    private String username;
    private String content;

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
} 