package com.confessly.repository;

import com.confessly.model.Komentar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface KomentarRepository extends JpaRepository<Komentar, Integer> {
    List<Komentar> findByMenfesIdOrderByCreatedAtDesc(int menfesId);
    List<Komentar> findByParentIdOrderByCreatedAtDesc(int parentId);
} 