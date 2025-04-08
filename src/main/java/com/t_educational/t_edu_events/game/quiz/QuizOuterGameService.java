package com.t_educational.t_edu_events.game.quiz;

import com.t_educational.t_edu_events.game.quiz.model.QuizConfig;
import com.t_educational.t_edu_events.game.quiz.model.QuizConfigEntity;
import com.t_educational.t_edu_events.game.quiz.repository.QuizConfigRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class QuizOuterGameService {

    private final QuizConfigRepository quizConfigRepository;

    public QuizOuterGameService(QuizConfigRepository quizConfigRepository) {
        this.quizConfigRepository = quizConfigRepository;
    }

    public void validateConfig(String configReference) {
        UUID configId = UUID.fromString(configReference);
        quizConfigRepository.findById(configId)
                .orElseThrow(() -> new RuntimeException("Quiz configuration not found"));
    }

    public int calculateMaxPoints(String configReference) {
        UUID configId = UUID.fromString(configReference);
        QuizConfigEntity configEntity = quizConfigRepository.findById(configId)
                .orElseThrow(() -> new RuntimeException("Quiz configuration not found"));
        QuizConfig config = configEntity.getConfigData();
        if (config.getQuestions() == null || config.getQuestions().isEmpty()) {
            throw new RuntimeException("No questions found in configuration");
        }
        return config.getQuestions().stream()
                .mapToInt(QuizConfig.Question::getPoints)
                .sum();
    }
}
