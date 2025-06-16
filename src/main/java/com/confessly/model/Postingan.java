package com.confessly.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass // Menandai Postingan sebagai superclass yang dipetakan
public abstract class Postingan { // Mengubah kelas menjadi abstract
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;
    
    @Column(nullable = false)
    protected String isi;
    
    @Column(name = "created_at", nullable = false)
    protected LocalDateTime createdAt;

    public Postingan() {
        this.createdAt = LocalDateTime.now();
    }

    public Postingan(String isi) { // Menghapus id dari konstruktor karena digenerate otomatis
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