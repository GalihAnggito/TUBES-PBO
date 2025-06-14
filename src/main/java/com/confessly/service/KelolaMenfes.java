package com.confessly.service;

import com.confessly.model.Menfes;
import com.confessly.model.User;
import com.confessly.model.Komentar;

public class KelolaMenfes {
    private MenfesService menfesService;

    public KelolaMenfes(MenfesService menfesService) {
        this.menfesService = menfesService;
    }

    public Menfes buatMenfes(String isi, User user) {
        return menfesService.buatMenfes(isi, user);
    }

    public boolean deleteMenfes(int id, User user) {
        return menfesService.deleteMenfes(id, user);
    }

    public int likeMenfes(int id, int userId) {
        return menfesService.likeMenfes(id, userId);
    }

    public boolean komenMenfes(int id, Komentar komentar) {
        return menfesService.komenMenfes(id, komentar);
    }
} 