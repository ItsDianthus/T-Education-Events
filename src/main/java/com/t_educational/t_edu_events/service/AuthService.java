package com.t_educational.t_edu_events.service;

import com.t_educational.t_edu_events.model.account.User;
import com.t_educational.t_edu_events.model.account.UserProfile;
import com.t_educational.t_edu_events.repository.UserRepository;
import com.t_educational.t_edu_events.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String register(String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Пользователь уже существует");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        // По умолчанию у нас роль обычного юзера при создании "ROLE_USER"
        user.setRole("ROLE_USER");

        UserProfile profile = new UserProfile();
        profile.setUser(user);
        user.setUserProfile(profile);

        userRepository.save(user);

        return jwtUtil.generateToken(email, user.getRole());
    }

    public String login(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return jwtUtil.generateToken(email, user.getRole());
            }
        }
        throw new RuntimeException("Неверный email или пароль");
    }
}
