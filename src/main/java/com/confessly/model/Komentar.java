package com.confessly.model;

import java.util.Date;

public class Komentar extends Postingan {
    private User pengirim;
    private Menfes menfes;
    private Date timestamp;

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
} 