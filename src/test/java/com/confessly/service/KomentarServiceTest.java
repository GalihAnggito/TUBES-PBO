package com.confessly.service;

import com.confessly.model.Komentar;
import com.confessly.model.User;
import com.confessly.repository.KomentarRepository;
import com.confessly.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class KomentarServiceTest {
    @Mock
    private KomentarRepository komentarRepository;

    @InjectMocks
    private KomentarService komentarService;

    private Komentar komentar;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User("testuser", "password");
        user.setId(1);
        komentar = new Komentar();
        komentar.setID(1);
        komentar.setPengirim(user);
    }

    @Test
    void testLikeKomentar_Success() {
        when(komentarRepository.findById(1)).thenReturn(Optional.of(komentar));
        int likes = komentarService.likeKomentar(1, user);
        assertEquals(1, likes);
    }

    @Test
    void testGetKomentar_Success() {
        when(komentarRepository.findById(1)).thenReturn(Optional.of(komentar));
        Komentar result = komentarService.getKomentar(1);
        assertNotNull(result);
        assertEquals(1, result.getID());
    }

    @Test
    void testGetKomentar_NotFound() {
        when(komentarRepository.findById(2)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> komentarService.getKomentar(2));
    }
} 