package com.confessly.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class Menfes extends Postingan {
    private int likes;
    private User pengirim;
    private List<Komentar> komentarList;
    private Date timestamp;
    private List<Integer> likedUserIds;

    public Menfes(int id, String isi, User pengirim) {
        super(id, isi);
        this.likes = 0;
        this.pengirim = pengirim;
        this.komentarList = new ArrayList<>();
        this.timestamp = new Date();
        this.likedUserIds = new ArrayList<>();
    }

    public int getLikes() {
        return likes;
    }

    public User getPengirim() {
        return pengirim;
    }

    public List<Komentar> getKomentarList() {
        return komentarList;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public List<Integer> getLikedUserIds() {
        return likedUserIds;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setPengirim(User pengirim) {
        this.pengirim = pengirim;
    }

    public void addKomentar(Komentar komentar) {
        this.komentarList.add(komentar);
    }

    public int tambahLike(int userId) {
        if (!likedUserIds.contains(userId)) {
            this.likes++;
            likedUserIds.add(userId);
            return this.likes;
        }
        return this.likes;
    }

    public boolean hasLiked(int userId) {
        return likedUserIds.contains(userId);
    }

    public List<String> getHashtags() {
        List<String> hashtags = new ArrayList<>();
        String[] words = this.getIsi().split("\\s+");
        for (String word : words) {
            if (word.startsWith("#")) {
                hashtags.add(word.toLowerCase());
            }
        }
        return hashtags;
    }
} 