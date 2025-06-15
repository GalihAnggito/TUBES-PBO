package com.confessly.service;

import com.confessly.model.User;
import com.confessly.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginLogout {
    private final UserRepository userRepository;

    @Autowired
    public LoginLogout(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public boolean logout(User user) {
        // Dengan JPA, logout biasanya tidak memerlukan interaksi langsung dengan DB
        // Ini lebih ke pengelolaan sesi di sisi client/Spring Security
        return true; // Asumsikan logout berhasil di sisi klien
    }

    public User register(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            return null; // Username sudah ada
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setRole("user"); // Default role user
        return userRepository.save(newUser);
    }

    public boolean isUserLoggedIn(User user) {
        if (user == null || user.getId() == 0) return false; // User tidak valid atau belum memiliki ID dari DB
        // Periksa apakah user masih ada di database berdasarkan ID
        // Atau bisa juga membandingkan dengan username dan password (jika disimpan di sesi)
        return userRepository.findById(user.getId()).isPresent();
    }

    public User getUserById(int userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public boolean isUsernameTaken(String username) {
        return userRepository.existsByUsername(username);
    }
} 