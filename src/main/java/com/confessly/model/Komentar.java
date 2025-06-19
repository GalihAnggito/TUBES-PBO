package com.confessly.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "komentar")
public class Komentar extends Postingan {
    private int likes;
    
    @ManyToOne
    @JoinColumn(name = "pengirim_id")
    private User pengirim;
    
    @ManyToOne
    @JoinColumn(name = "menfes_id")
    @JsonIgnore
    private Menfes menfes;
    
    @ManyToMany
    @JoinTable(
        name = "komentar_likes",
        joinColumns = @JoinColumn(name = "komentar_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> likedUsers = new ArrayList<>();
    
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Komentar> replies = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private Komentar parent;

    public Komentar() {
        super();
        this.likes = 0;
    }

    public Komentar(String isi, User pengirim, Menfes menfes) {
        super(isi);
        this.pengirim = pengirim;
        this.menfes = menfes;
        this.likes = 0;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public User getPengirim() {
        return pengirim;
    }

    public void setPengirim(User pengirim) {
        this.pengirim = pengirim;
    }

    public Menfes getMenfes() {
        return menfes;
    }

    public void setMenfes(Menfes menfes) {
        this.menfes = menfes;
    }

    public List<User> getLikedUsers() {
        return likedUsers;
    }

    public void setLikedUsers(List<User> likedUsers) {
        this.likedUsers = likedUsers;
    }

    public List<Komentar> getReplies() {
        return replies;
    }

    public void setReplies(List<Komentar> replies) {
        this.replies = replies;
    }

    public Komentar getParent() {
        return parent;
    }

    public void setParent(Komentar parent) {
        this.parent = parent;
    }

    public int tambahLike(User user) {
        if (likedUsers.contains(user)) {
            this.likes--;
            likedUsers.remove(user);
        } else {
            this.likes++;
            likedUsers.add(user);
        }
        return this.likes;
    }

    public boolean hasLiked(User user) {
        return likedUsers.contains(user);
    }

    public void addReply(Komentar reply) {
        reply.setParent(this);
        this.replies.add(reply);
    }

    public List<Integer> getLikedUserIds() {
        return likedUsers.stream()
                .map(User::getId)
                .collect(Collectors.toList());
    }

    public String getAnonymousUsername() {
        return "anonymous";
    }
} 