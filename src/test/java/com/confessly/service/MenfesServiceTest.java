package com.confessly.service;

import com.confessly.model.Menfes;
import com.confessly.model.User;
import com.confessly.model.Komentar;
import com.confessly.repository.MenfesRepository;
import com.confessly.repository.UserRepository;
import com.confessly.repository.KomentarRepository;
import com.confessly.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MenfesServiceTest {
    @Mock
    private MenfesRepository menfesRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private KomentarRepository komentarRepository;
    @Mock
    private LoginLogout authService;

    @InjectMocks
    private MenfesService menfesService;

    private User user;
    private Menfes menfes;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User("testuser", "password");
        user.setId(1);
        menfes = new Menfes("Test menfess", user);
        menfes.setID(1);
    }

    @Test
    void testBuatMenfes() {
        when(menfesRepository.save(any(Menfes.class))).thenReturn(menfes);
        Menfes result = menfesService.buatMenfes("Test menfess", user);
        assertNotNull(result);
        assertEquals("Test menfess", result.getIsi());
        assertEquals(user, result.getPengirim());
    }

    @Test
    void testDeleteMenfes_Success() {
        when(menfesRepository.findById(1)).thenReturn(Optional.of(menfes));
        user.setRole(new com.confessly.model.Role("user"));
        boolean result = menfesService.deleteMenfes(1, user);
        assertTrue(result);
        verify(menfesRepository).delete(menfes);
    }

    @Test
    void testDeleteMenfes_Fail_NotOwner() {
        User otherUser = new User("other", "pass");
        otherUser.setId(2);
        when(menfesRepository.findById(1)).thenReturn(Optional.of(menfes));
        boolean result = menfesService.deleteMenfes(1, otherUser);
        assertFalse(result);
        verify(menfesRepository, never()).delete(any());
    }

    @Test
    void testEditMenfes_Success() {
        when(menfesRepository.findById(1)).thenReturn(Optional.of(menfes));
        when(menfesRepository.save(any(Menfes.class))).thenReturn(menfes);
        Menfes result = menfesService.editMenfes(1, "Updated content", user);
        assertEquals("Updated content", result.getIsi());
    }

    @Test
    void testEditMenfes_Fail_NotOwner() {
        User otherUser = new User("other", "pass");
        otherUser.setId(2);
        when(menfesRepository.findById(1)).thenReturn(Optional.of(menfes));
        assertThrows(IllegalStateException.class, () -> {
            menfesService.editMenfes(1, "Updated content", otherUser);
        });
    }

    @Test
    void testLikeMenfess() {
        when(menfesRepository.findById(1)).thenReturn(Optional.of(menfes));
        when(menfesRepository.save(any(Menfes.class))).thenReturn(menfes);
        int likes = menfesService.likeMenfess(1, user);
        assertEquals(1, likes);
    }

    @Test
    void testLihatMenfesTerbaru() {
        when(menfesRepository.findAllByOrderByCreatedAtDesc()).thenReturn(Collections.singletonList(menfes));
        List<Menfes> result = menfesService.lihatMenfesTerbaru();
        assertEquals(1, result.size());
        assertEquals(menfes, result.get(0));
    }
} 