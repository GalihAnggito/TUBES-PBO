package com.confessly.service;

import com.confessly.model.Komentar;
import com.confessly.model.User;
import com.confessly.repository.KomentarRepository;
import com.confessly.exception.ResourceNotFoundException;
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
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id " + id));
        return komentar.tambahLike(user);
    }

    public Komentar getKomentar(int id) {
        return komentarRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id " + id));
    }

    @Transactional
    public boolean deleteKomentar(int id, User user) {
        Komentar komentar = komentarRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id " + id));
        
        if (komentar.getPengirim().getId() == user.getId() || user.getRole().getRoleName().equals("admin")) {
            komentarRepository.delete(komentar);
            return true;
        }
        return false;
    }
} 