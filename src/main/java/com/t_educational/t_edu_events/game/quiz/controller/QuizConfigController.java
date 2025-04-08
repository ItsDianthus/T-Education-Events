package com.t_educational.t_edu_events.game.quiz.controller;

import com.t_educational.t_edu_events.game.quiz.model.QuizConfig;
import com.t_educational.t_edu_events.game.quiz.model.QuizConfigEntity;
import com.t_educational.t_edu_events.game.quiz.repository.QuizConfigRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/quiz-configurations")
public class QuizConfigController {

    private final QuizConfigRepository quizConfigRepository;

    public QuizConfigController(QuizConfigRepository quizConfigRepository) {
        this.quizConfigRepository = quizConfigRepository;
    }

    // POST /api/admin/quiz-configurations — создание нового конфига
    @PostMapping
    public ResponseEntity<QuizConfigEntity> createQuizConfig(@RequestBody QuizConfig config) {
        QuizConfigEntity entity = new QuizConfigEntity();
        entity.setConfigData(config);
        QuizConfigEntity savedEntity = quizConfigRepository.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEntity);
    }

    // GET /api/admin/quiz-configurations — получение всех конфигураций
    @GetMapping
    public ResponseEntity<List<QuizConfigEntity>> getAllQuizConfigs() {
        List<QuizConfigEntity> configs = quizConfigRepository.findAll();
        return ResponseEntity.ok(configs);
    }

    // GET /api/admin/quiz-configurations/{configId} - получение конкретной конфигурации
    @GetMapping("/{configId}")
    public ResponseEntity<QuizConfigEntity> getQuizConfig(@PathVariable UUID configId) {
        QuizConfigEntity config = quizConfigRepository.findById(configId)
                .orElseThrow(() -> new RuntimeException("Quiz configuration not found"));
        return ResponseEntity.ok(config);
    }

    // DELETE /api/admin/quiz-configurations/{configId} — удаление конфигурации
    @DeleteMapping("/{configId}")
    public ResponseEntity<Void> deleteQuizConfig(@PathVariable UUID configId) {
        if (!quizConfigRepository.existsById(configId)) {
            return ResponseEntity.notFound().build();
        }
        quizConfigRepository.deleteById(configId);
        return ResponseEntity.noContent().build();
    }
}
