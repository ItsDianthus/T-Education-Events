package com.t_educational.t_edu_events.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
public class TeduInfoController {

    private final ResourceLoader resourceLoader;

    @Autowired
    public TeduInfoController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @GetMapping(value = "/api/tedu/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getTeduInfo() {
        try {
            Resource resource = resourceLoader.getResource("classpath:tedu-info.json");
            String jsonData = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
            return ResponseEntity.ok(jsonData);
        } catch (IOException e) {
            return ResponseEntity.status(500)
                    .body("{\"error\": \"Unable to load T-edu information\"}");
        }
    }
}
