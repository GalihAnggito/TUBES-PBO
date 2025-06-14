package com.confessly.service;

import com.confessly.model.Menfes;
import com.confessly.model.User;
import com.confessly.model.Komentar;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class MenfesService {
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

    public int likeMenfes(int id, int userId) {
        for (Menfes menfes : menfessList) {
            if (menfes.getID() == id) {
                return menfes.tambahLike(userId);
            }
        }
        return 0;
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
            // Calculate popularity score based on likes and comments
            int score1 = m1.getLikes() + m1.getKomentarList().size();
            int score2 = m2.getLikes() + m2.getKomentarList().size();
            return Integer.compare(score2, score1); // Sort in descending order
        });
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
} 