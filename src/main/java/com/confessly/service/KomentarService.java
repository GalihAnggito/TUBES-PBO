package com.confessly.service;

import com.confessly.model.Komentar;
import com.confessly.model.User;
import com.confessly.repository.KomentarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KomentarService {
    @Autowired
    private KomentarRepository komentarRepository;

    @Transactional
    public int likeKomentar(int id, User user) {
        Komentar komentar = komentarRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Comment not found"));
        return komentar.tambahLike(user);
    }

    public Komentar getKomentar(int id) {
        return komentarRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Comment not found"));
    }
} 