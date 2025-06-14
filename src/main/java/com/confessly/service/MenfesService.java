package com.confessly.service;

import com.confessly.model.Menfes;
import com.confessly.model.User;
import com.confessly.model.Komentar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class MenfesService {
    private static final Logger logger = LoggerFactory.getLogger(MenfesService.class);
    private List<Menfes> menfessList;
    private int nextId;
    private LoginLogout authService;

    public MenfesService() {
        this.menfessList = new ArrayList<>();
        this.nextId = 1;
        this.authService = new LoginLogout();
    }

    public Menfes buatMenfes(String isi, User user) {
        if (!authService.isUserLoggedIn(user)) {
            throw new IllegalStateException("User must be logged in to create a menfess post");
        }
        Menfes menfes = new Menfes(nextId++, isi, user);
        menfessList.add(menfes);
        return menfes;
    }

    public boolean deleteMenfes(int id, User user) {
        for (int i = 0; i < menfessList.size(); i++) {
            Menfes menfes = menfessList.get(i);
            if (menfes.getID() == id) {
                if (menfes.getPengirim().getId() == user.getId() || "admin".equals(user.getRole())) {
                    menfessList.remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    public Menfes editMenfes(int id, String newContent, User user) {
        for (Menfes menfes : menfessList) {
            if (menfes.getID() == id) {
                if (menfes.getPengirim().getId() == user.getId() || "admin".equals(user.getRole())) {
                    menfes.setIsi(newContent);
                    return menfes;
                }
                throw new IllegalStateException("You don't have permission to edit this menfess");
            }
        }
        throw new IllegalStateException("Menfess not found");
    }

    public int likeMenfes(int id, int userId) {
        for (Menfes menfes : menfessList) {
            if (menfes.getID() == id) {
                if (menfes.hasLiked(userId)) {
                    throw new IllegalStateException("You have already liked this menfess");
                }
                return menfes.tambahLike(userId);
            }
        }
        throw new IllegalStateException("Menfess not found");
    }

    public boolean komenMenfes(int id, Komentar komentar) {
        for (Menfes menfes : menfessList) {
            if (menfes.getID() == id) {
                menfes.addKomentar(komentar);
                return true;
            }
        }
        return false;
    }

    public List<Menfes> lihatMenfesTerbaru() {
        List<Menfes> sortedList = new ArrayList<>(menfessList);
        sortedList.sort((m1, m2) -> m2.getTimestamp().compareTo(m1.getTimestamp()));
        return sortedList;
    }

    public List<Menfes> lihatMenfesPopuler() {
        List<Menfes> sortedList = new ArrayList<>(menfessList);
        sortedList.sort((m1, m2) -> {
            int score1 = (m1.getLikes() * 2) + m1.getKomentarList().size();
            int score2 = (m2.getLikes() * 2) + m2.getKomentarList().size();
            int scoreComparison = Integer.compare(score2, score1); // descending
            if (scoreComparison != 0) {
                return scoreComparison;
            }
            // Jika skor sama, urutkan berdasarkan timestamp terbaru
            return m2.getTimestamp().compareTo(m1.getTimestamp());
        });
        // Logging urutan hasil sorting
        logger.info("[POPULAR SORT] Urutan hasil sorting:");
        for (Menfes m : sortedList) {
            logger.info("ID: {} | Likes: {} | Komentar: {} | Score: {}", m.getID(), m.getLikes(), m.getKomentarList().size(), (m.getLikes()*2)+m.getKomentarList().size());
        }
        return sortedList;
    }

    public List<String> getTrendingHashtags() {
        Map<String, Integer> hashtagScores = new HashMap<>();
        
        // Calculate score for each hashtag based on likes
        for (Menfes menfes : menfessList) {
            for (String hashtag : menfes.getHashtags()) {
                hashtagScores.merge(hashtag, menfes.getLikes(), Integer::sum);
            }
        }
        
        // Sort hashtags by score
        return hashtagScores.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(8) // Get top 8 trending hashtags
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    public List<Menfes> lihatMenfesSaya(String username) {
        List<Menfes> result = new ArrayList<>();
        for (Menfes m : menfessList) {
            if (m.getPengirim() != null && m.getPengirim().getUsername().equals(username)) {
                result.add(m);
            }
        }
        // Urutkan dari terbaru
        result.sort((m1, m2) -> m2.getTimestamp().compareTo(m1.getTimestamp()));
        return result;
    }
} 