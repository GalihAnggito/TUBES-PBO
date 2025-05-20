package com.confessly.service;

import com.confessly.model.Menfes;
import com.confessly.model.User;
import com.confessly.model.Komentar;
import java.util.ArrayList;
import java.util.List;

public class MenfesService {
    private List<Menfes> menfessList;
    private int nextId;

    public MenfesService() {
        this.menfessList = new ArrayList<>();
        this.nextId = 1;
    }

    public Menfes buatMenfes(String isi, User user) {
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

    public int likeMenfes(int id) {
        for (Menfes menfes : menfessList) {
            if (menfes.getID() == id) {
                return menfes.tambahLike();
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
        return new ArrayList<>(menfessList);
    }
} 