package com.t_educational.t_edu_events.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TestController {

    @GetMapping("/public/api/greeting")
    public Map<String, String> greeting() {
        return Map.of("message", "Привет, REST!", "message2", "Hello");
    }
}
