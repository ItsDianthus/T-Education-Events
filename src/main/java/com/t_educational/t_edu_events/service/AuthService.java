package com.t_educational.t_edu_events.service;

import com.t_educational.t_edu_events.model.account.User;
import com.t_educational.t_edu_events.model.account.UserProfile;
import com.t_educational.t_edu_events.repository.account.UserRepository;
import com.t_educational.t_edu_events.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}|\\[\\]:;\"'<>,.?/]).{10,}$");

    public String register(String email, String password) {
        validateEmail(email);
        validatePassword(password);

        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Пользователь уже существует");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        // По умолчанию у нас роль обычного пользователя: "ROLE_USER"
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

    private void validateEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Неверный формат email. Email должен содержать латинские буквы, цифры, допустимые спецсимволы и символ '@'.");
        }
    }

    private void validatePassword(String password) {
        if (password == null || !PASSWORD_PATTERN.matcher(password).matches()) {
            throw new IllegalArgumentException("Пароль должен быть не короче 10 символов и содержать хотя бы одну цифру, одну строчную и одну заглавную буквы, а также один специальный символ.");
        }
        String lowerPassword = password.toLowerCase();
        if (lowerPassword.contains("123") || lowerPassword.contains("12345") || lowerPassword.contains("qwerty") || lowerPassword.contains("password")) {
            throw new IllegalArgumentException("Пароль является слишком простым, выберите более сложный вариант.");
        }
    }
}
