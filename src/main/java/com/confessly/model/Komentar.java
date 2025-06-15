package com.confessly.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "komentar")
public class Komentar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String isi;
    
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
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
        this.createdAt = new Date();
    }

    public Komentar(int id, String isi, User pengirim, Menfes menfes) {
        this.id = id;
        this.isi = isi;
        this.pengirim = pengirim;
        this.menfes = menfes;
        this.createdAt = new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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
        if (!likedUsers.contains(user)) {
            likedUsers.add(user);
        }
        return likedUsers.size();
    }

    public boolean hasLiked(User user) {
        return likedUsers.contains(user);
    }

    public void addReply(Komentar reply) {
        reply.setParent(this);
        this.replies.add(reply);
    }
} 