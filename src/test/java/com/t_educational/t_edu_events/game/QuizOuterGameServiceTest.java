package com.t_educational.t_edu_events.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.t_educational.t_edu_events.game.quiz.service.QuizOuterGameService;
import com.t_educational.t_edu_events.game.quiz.model.QuizConfig;
import com.t_educational.t_edu_events.game.quiz.model.QuizConfigEntity;
import com.t_educational.t_edu_events.game.quiz.repository.QuizConfigRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class QuizOuterGameServiceTest {

    @Mock
    private QuizConfigRepository quizConfigRepository;

    @InjectMocks
    private QuizOuterGameService quizOuterGameService;

    private QuizConfigEntity configEntity;
    private QuizConfig config;
    private UUID configId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        configId = UUID.randomUUID();
        QuizConfig.Question question1 = new QuizConfig.Question("Question 1", List.of("A", "B"), List.of("A"), 10);
        QuizConfig.Question question2 = new QuizConfig.Question("Question 2", List.of("C", "D"), List.of("C"), 15);
        config = new QuizConfig(List.of(question1, question2));
        configEntity = new QuizConfigEntity();
        configEntity.setConfigId(configId);
        configEntity.setConfigData(config);
    }

    @Test
    void testValidateConfigSuccess() {
        String configReference = configId.toString();
        when(quizConfigRepository.findById(configId)).thenReturn(Optional.of(configEntity));

        quizOuterGameService.validateConfig(configReference);

        verify(quizConfigRepository, times(1)).findById(configId);
    }

    @Test
    void testValidateConfigNotFound() {
        String configReference = configId.toString();
        when(quizConfigRepository.findById(configId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                quizOuterGameService.validateConfig(configReference));
        assertEquals("Quiz configuration not found", exception.getMessage());

        verify(quizConfigRepository, times(1)).findById(configId);
    }

    @Test
    void testCalculateMaxPointsSuccess() {
        String configReference = configId.toString();
        when(quizConfigRepository.findById(configId)).thenReturn(Optional.of(configEntity));

        int maxPoints = quizOuterGameService.calculateMaxPoints(configReference);
        assertEquals(25, maxPoints);

        verify(quizConfigRepository, times(1)).findById(configId);
    }

    @Test
    void testCalculateMaxPointsConfigNotFound() {
        String configReference = configId.toString();
        when(quizConfigRepository.findById(configId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                quizOuterGameService.calculateMaxPoints(configReference));
        assertEquals("Quiz configuration not found", exception.getMessage());

        verify(quizConfigRepository, times(1)).findById(configId);
    }

    @Test
    void testCalculateMaxPointsNoQuestions() {
        String configReference = configId.toString();
        QuizConfig emptyConfig = new QuizConfig(new ArrayList<>());
        configEntity.setConfigData(emptyConfig);
        when(quizConfigRepository.findById(configId)).thenReturn(Optional.of(configEntity));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                quizOuterGameService.calculateMaxPoints(configReference));
        assertEquals("No questions found in configuration", exception.getMessage());

        verify(quizConfigRepository, times(1)).findById(configId);
    }

    @Test
    void testCalculateMaxPointsNullQuestions() {
        String configReference = configId.toString();
        QuizConfig nullConfig = new QuizConfig(null);
        configEntity.setConfigData(nullConfig);
        when(quizConfigRepository.findById(configId)).thenReturn(Optional.of(configEntity));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                quizOuterGameService.calculateMaxPoints(configReference));
        assertEquals("No questions found in configuration", exception.getMessage());

        verify(quizConfigRepository, times(1)).findById(configId);
    }
}
