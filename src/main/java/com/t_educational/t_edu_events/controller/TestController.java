package com.t_educational.t_edu_events.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TestController {

    @GetMapping("/api/public/greeting")
    public Map<String, String> greeting() {
        return Map.of("message", "Привет, public!", "message2", "Hello");
    }

    @GetMapping("/api/admin/greeting")
    public Map<String, String> greeting2() {
        return Map.of("message", "Привет, admin!", "message2", "Hello");
    }

    @GetMapping("/api/greeting")
    public Map<String, String> greeting3() {
        return Map.of("message", "Привет, user!", "message2", "Hello");
    }
}
