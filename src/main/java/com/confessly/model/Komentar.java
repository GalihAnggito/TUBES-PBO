package com.confessly.model;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class Komentar extends Postingan {
    private User pengirim;
    private Menfes menfes;
    private Date timestamp;
    private List<Integer> likedUserIds = new ArrayList<>();
    private List<Komentar> replies = new ArrayList<>();

    public Komentar(int id, String isi, User pengirim, Menfes menfes) {
        super(id, isi);
        this.pengirim = pengirim;
        this.menfes = menfes;
        this.timestamp = new Date();
    }

    public User getPengirim() {
        return pengirim;
    }

    public Menfes getMenfes() {
        return menfes;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setPengirim(User pengirim) {
        this.pengirim = pengirim;
    }

    public void setMenfes(Menfes menfes) {
        this.menfes = menfes;
    }

    public int tambahLike(int userId) {
        if (!likedUserIds.contains(userId)) {
            likedUserIds.add(userId);
        }
        return likedUserIds.size();
    }

    public boolean hasLiked(int userId) {
        return likedUserIds.contains(userId);
    }

    public void addReply(Komentar reply) {
        this.replies.add(reply);
    }

    public List<Komentar> getReplies() {
        return replies;
    }
} 