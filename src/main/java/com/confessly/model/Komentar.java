package com.confessly.model;

public class Komentar extends Postingan {
    private User pengirim;
    private Menfes menfes;

    public Komentar(int id, String isi, User pengirim, Menfes menfes) {
        super(id, isi);
        this.pengirim = pengirim;
        this.menfes = menfes;
    }

    public User getPengirim() {
        return pengirim;
    }

    public Menfes getMenfes() {
        return menfes;
    }

    public void setPengirim(User pengirim) {
        this.pengirim = pengirim;
    }

    public void setMenfes(Menfes menfes) {
        this.menfes = menfes;
    }
} 