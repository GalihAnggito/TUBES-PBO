package com.confessly.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "menfes")
public class Menfes extends Postingan {
    private int likes;
    
    @ManyToOne
    @JoinColumn(name = "pengirim_id")
    private User pengirim;
    
    @OneToMany(mappedBy = "menfes", cascade = CascadeType.ALL)
    private List<Komentar> komentarList = new ArrayList<>();
    
    @ManyToMany
    @JoinTable(
        name = "menfes_likes",
        joinColumns = @JoinColumn(name = "menfes_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> likedUsers = new ArrayList<>();

    public Menfes() {
        super();
        this.likes = 0;
    }

    public Menfes(String isi, User pengirim) {
        super(isi);
        this.pengirim = pengirim;
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

    public List<Komentar> getKomentarList() {
        return komentarList;
    }

    public void setKomentarList(List<Komentar> komentarList) {
        this.komentarList = komentarList;
    }

    public void addKomentar(Komentar komentar) {
        this.komentarList.add(komentar);
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

    public List<String> getHashtags() {
        List<String> hashtags = new ArrayList<>();
        String[] words = this.isi.split("\\s+");
        for (String word : words) {
            if (word.startsWith("#")) {
                hashtags.add(word.toLowerCase());
            }
        }
        return hashtags;
    }

    public List<User> getLikedUsers() {
        return likedUsers;
    }

    public List<Integer> getLikedUserIds() {
        return likedUsers.stream()
                .map(User::getId)
                .collect(Collectors.toList());
    }

    public void setLikedUsers(List<User> likedUsers) {
        this.likedUsers = likedUsers;
    }
} 