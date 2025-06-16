package com.confessly.service;

import com.confessly.model.Komentar;
import com.confessly.model.Menfes;
import com.confessly.model.User;
import java.util.List;

public interface MentosService {
    Menfes buatMenfes(String isi, User user);
    boolean deleteMenfes(int id, User user);
    Menfes editMenfes(int id, String newContent, User user);
    int likeMenfess(int id, User user);
    boolean komenMenfes(int id, Komentar komentar);
    List<Menfes> lihatMenfesTerbaru();
    List<Menfes> lihatMenfesPopuler();
    List<String> getTrendingHashtags();
    List<Menfes> lihatMenfesSaya(String username);
    Komentar getKomentarById(int id);
    Menfes getMenfess(int id);
    List<Menfes> getMenfessByUserId(int userId);
} 