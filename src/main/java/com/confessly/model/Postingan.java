package com.confessly.model;

import java.time.LocalDateTime;

public class Postingan {
    private int id;
    private String isi;
    private LocalDateTime createdAt;

    public Postingan(int id, String isi) {
        this.id = id;
        this.isi = isi;
        this.createdAt = LocalDateTime.now();
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
} 