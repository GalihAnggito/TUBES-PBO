package com.confessly.repository;

import com.confessly.model.Menfes;
import com.confessly.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MenfesRepository extends JpaRepository<Menfes, Integer> {
    List<Menfes> findAllByOrderByCreatedAtDesc();
    
    @Query("SELECT m FROM Menfes m ORDER BY (m.likes * 2 + SIZE(m.komentarList)) DESC")
    List<Menfes> findAllOrderByPopularity();

    List<Menfes> findByPengirim(User pengirim);
    
    List<Menfes> findByPengirimId(int userId);
} 