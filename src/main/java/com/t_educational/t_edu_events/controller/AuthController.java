package com.t_educational.t_edu_events.controller;

import com.t_educational.t_edu_events.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        try {
            String token = authService.register(email, password);
            return ResponseEntity.ok(Map.of("token", token)); // ✅ 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage())); // ❌ 400 Bad Request
        }

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        try {
            String token = authService.login(email, password);
            return ResponseEntity.ok(Map.of("token", token)); // ✅ 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Неверный email или пароль")); // ❌ 401 Unauthorized
        }

    }
}
