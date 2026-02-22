package com.planner.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.planner.model.User;
import com.planner.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public User registerUser(String name, String email, String password) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }
    
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public void updateStreak(User user) {

        LocalDate today = LocalDate.now();

        if (user.getLastActivityDate() == null) {
            user.setStreak(1);
        } else {

            LocalDate lastDate = user.getLastActivityDate();

            if (lastDate.equals(today)) {
                // already counted today
                return;
            }

            if (lastDate.equals(today.minusDays(1))) {
                user.setStreak(user.getStreak() + 1);
            } else {
                user.setStreak(1);
            }
        }

        user.setLastActivityDate(today);
    }
}