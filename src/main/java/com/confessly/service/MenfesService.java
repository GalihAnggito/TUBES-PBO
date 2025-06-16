package com.confessly.service;

import com.confessly.model.Menfes;
import com.confessly.model.User;
import com.confessly.model.Komentar;
import com.confessly.repository.MenfesRepository;
import com.confessly.repository.UserRepository;
import com.confessly.repository.KomentarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class MenfesService {
    private static final Logger logger = LoggerFactory.getLogger(MenfesService.class);
    private final MenfesRepository menfesRepository;
    private final UserRepository userRepository;
    private final KomentarRepository komentarRepository;
    private LoginLogout authService;

    @Autowired
    public MenfesService(MenfesRepository menfesRepository, UserRepository userRepository, KomentarRepository komentarRepository, LoginLogout authService) {
        this.menfesRepository = menfesRepository;
        this.userRepository = userRepository;
        this.komentarRepository = komentarRepository;
        this.authService = authService;
    }

    @Transactional
    public Menfes buatMenfes(String isi, User user) {
        Menfes menfes = new Menfes();
        menfes.setIsi(isi);
        menfes.setPengirim(user);
        return menfesRepository.save(menfes);
    }

    @Transactional
    public boolean deleteMenfes(int id, User user) {
        Menfes menfes = menfesRepository.findById(id).orElse(null);
        if (menfes != null && (menfes.getPengirim().getId() == user.getId() || user.getRole().equals("admin"))) {
            menfesRepository.delete(menfes);
            return true;
        }
        return false;
    }

    @Transactional
    public Menfes editMenfes(int id, String newContent, User user) {
        Menfes menfes = menfesRepository.findById(id).orElse(null);
        if (menfes != null) {
            if (menfes.getPengirim().getId() == user.getId() || "admin".equals(user.getRole())) {
                menfes.setIsi(newContent);
                return menfesRepository.save(menfes);
            }
            throw new IllegalStateException("You don't have permission to edit this menfess");
        }
        throw new IllegalStateException("Menfess not found");
    }

    @Transactional
    public int likeMenfess(int id, User user) {
        Menfes menfes = menfesRepository.findById(id).orElse(null);
        if (menfes != null) {
            int currentLikes = menfes.tambahLike(user);
            menfesRepository.save(menfes);
            return currentLikes;
        }
        return 0;
    }

    @Transactional
    public boolean komenMenfes(int id, Komentar komentar) {
        Menfes menfes = menfesRepository.findById(id).orElse(null);
        if (menfes != null) {
            komentar.setMenfes(menfes);
            komentarRepository.save(komentar);
            menfes.getKomentarList().add(komentar);
            menfesRepository.save(menfes);
            return true;
        }
        return false;
    }

    public List<Menfes> lihatMenfesTerbaru() {
        return menfesRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Menfes> lihatMenfesPopuler() {
        return menfesRepository.findAllOrderByPopularity();
    }

    public List<String> getTrendingHashtags() {
        Map<String, Integer> hashtagScores = new HashMap<>();
        
        List<Menfes> allMenfess = menfesRepository.findAll();
        for (Menfes menfes : allMenfess) {
            for (String hashtag : menfes.getHashtags()) {
                hashtagScores.merge(hashtag, menfes.getLikes(), Integer::sum);
            }
        }
        
        return hashtagScores.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(8)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    public List<Menfes> lihatMenfesSaya(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return menfesRepository.findByPengirim(user);
        }
        return new ArrayList<>();
    }

    public Komentar getKomentarById(int id) {
        return komentarRepository.findById(id).orElse(null);
    }

    public Menfes getMenfess(int id) {
        return menfesRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Menfess not found"));
    }

    public List<Menfes> getMenfessByUserId(int userId) {
        return menfesRepository.findByPengirimId(userId);
    }
} 