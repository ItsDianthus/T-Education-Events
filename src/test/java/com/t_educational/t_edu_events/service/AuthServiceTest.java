package com.t_educational.t_edu_events.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.t_educational.t_edu_events.model.account.User;
import com.t_educational.t_edu_events.repository.account.UserRepository;
import com.t_educational.t_edu_events.security.JwtUtil;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterSuccess() {
        String email = "test@example.com";
        String rawPassword = "Valid$Passw0rd";
        String encodedPassword = "encodedPassword";
        String expectedToken = "jwtToken123";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(jwtUtil.generateToken(email, "ROLE_USER")).thenReturn(expectedToken);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String actualToken = authService.register(email, rawPassword);
        assertEquals(expectedToken, actualToken);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUserAlreadyExists() {
        String email = "test@example.com";
        String rawPassword = "Valid$Passw2rd213";
        User existingUser = new User();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.register(email, rawPassword));
        assertEquals("Пользователь уже существует", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegisterInvalidEmail() {
        String invalidEmail = "invalid-email";
        String rawPassword = "Valid$Passwodd1#3";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> authService.register(invalidEmail, rawPassword));
        assertEquals("Неверный формат email. Email должен содержать латинские буквы, цифры, допустимые спецсимволы и символ '@'.",
                exception.getMessage());
    }

    @Test
    void testRegisterInvalidPassword() {
        String email = "test@example.com";
        String invalidPassword = "weakpass";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> authService.register(email, invalidPassword));
        assertEquals("Пароль должен быть не короче 10 символов и содержать хотя бы одну цифру, одну строчную и одну заглавную буквы, а также один специальный символ.",
                exception.getMessage());
    }

    @Test
    void testLoginSuccess() {
        String email = "test@example.com";
        String rawPassword = "Valid$Pas$word113";
        String encodedPassword = "encodedPassword";
        String expectedToken = "jwtToken123";

        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setRole("ROLE_USER");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);
        when(jwtUtil.generateToken(email, "ROLE_USER")).thenReturn(expectedToken);

        String actualToken = authService.login(email, rawPassword);
        assertEquals(expectedToken, actualToken);
    }

    @Test
    void testLoginInvalidEmail() {
        String email = "nonexistent@example.com";
        String rawPassword = "Valid$PasSSsword1323";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.login(email, rawPassword));
        assertEquals("Неверный email или пароль", exception.getMessage());
    }

    @Test
    void testLoginIncorrectPassword() {
        String email = "test@example.com";
        String rawPassword = "Valid$Password123";
        String encodedPassword = "encodedPassword";

        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setRole("ROLE_USER");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.login(email, rawPassword));
        assertEquals("Неверный email или пароль", exception.getMessage());
    }
}
