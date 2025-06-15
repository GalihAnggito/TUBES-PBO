package com.confessly.controller;

import com.confessly.service.MenfesService;
import com.confessly.model.Menfes;
import com.confessly.model.User;
import com.confessly.model.Komentar;
import java.util.List;

public class KontrolMenfes {
    private MenfesService service;

    public KontrolMenfes(MenfesService service) {
        this.service = service;
    }

    public Menfes buatMenfes(String isi, User user) {
        return service.buatMenfes(isi, user);
    }

    public boolean deleteMenfes(int id, User user) {
        return service.deleteMenfes(id, user);
    }

    public int likeMenfes(int id, User user) {
        return service.likeMenfess(id, user);
    }

    public boolean komenMenfes(int id, String isi, User user) {
        Menfes menfes = service.lihatMenfesTerbaru().stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .orElse(null);

        if (menfes != null) {
            Komentar komentar = new Komentar();
            komentar.setIsi(isi);
            komentar.setPengirim(user);
            komentar.setMenfes(menfes);
            return service.komenMenfes(id, komentar);
        }
        return false;
    }

    public List<Menfes> lihatMenfesTerbaru() {
        return service.lihatMenfesTerbaru();
    }
} 