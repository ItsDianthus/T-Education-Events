package com.t_educational.t_edu_events.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.t_educational.t_edu_events.game.quiz.service.QuizGameService;
import com.t_educational.t_edu_events.game.quiz.dto.AnswerRequest;
import com.t_educational.t_edu_events.game.quiz.dto.AnswerResponse;
import com.t_educational.t_edu_events.game.quiz.dto.QuestionResponse;
import com.t_educational.t_edu_events.game.quiz.model.QuizConfig;
import com.t_educational.t_edu_events.game.quiz.model.QuizConfigEntity;
import com.t_educational.t_edu_events.game.quiz.model.QuizGameSession;
import com.t_educational.t_edu_events.game.quiz.repository.QuizConfigRepository;
import com.t_educational.t_edu_events.game.quiz.repository.QuizGameSessionRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class QuizGameServiceTest {

    @Mock
    private QuizConfigRepository quizConfigRepository;

    @Mock
    private QuizGameSessionRepository quizGameSessionRepository;

    @InjectMocks
    private QuizGameService quizGameService;

    private QuizConfigEntity configEntity;
    private QuizConfig config;
    private QuizConfig.Question question;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        question = new QuizConfig.Question(
                "What is the capital of France?",
                List.of("Paris", "London", "Berlin"),
                List.of("Paris"),
                10
        );
        config = new QuizConfig(List.of(question));
        configEntity = new QuizConfigEntity();
        configEntity.setConfigId(UUID.randomUUID());
        configEntity.setConfigData(config);
    }

    @Test
    void testStartSessionSuccess() {
        UUID configId = configEntity.getConfigId();
        String configReference = configId.toString();
        UUID userId = UUID.randomUUID();
        UUID gameId = UUID.randomUUID();
        String externalSessionReference = "externalSessionRef123";
        when(quizConfigRepository.findById(configId)).thenReturn(Optional.of(configEntity));
        QuizGameSession savedSession = new QuizGameSession();
        savedSession.setSessionId(UUID.randomUUID());
        savedSession.setUserId(userId);
        savedSession.setGameId(gameId);
        savedSession.setGameSessionIdReference(externalSessionReference);
        savedSession.setCurrentQuestion(1);
        savedSession.setTotalPoints(0);
        savedSession.setConfigId(configId);
        when(quizGameSessionRepository.save(any(QuizGameSession.class)))
                .thenReturn(savedSession);
        String jsonResponse = quizGameService.startSession(configReference, userId, gameId, externalSessionReference);
        assertEquals(true, jsonResponse.contains("What is the capital of France?"));
        assertEquals(true, jsonResponse.contains(savedSession.getSessionId().toString()));
    }

    @Test
    void testStartSessionConfigNotFound() {
        UUID configId = UUID.randomUUID();
        String configReference = configId.toString();
        UUID userId = UUID.randomUUID();
        UUID gameId = UUID.randomUUID();
        String externalSessionReference = "extSession";
        when(quizConfigRepository.findById(configId)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                quizGameService.startSession(configReference, userId, gameId, externalSessionReference));
        assertEquals("Quiz configuration not found", exception.getMessage());
    }

    @Test
    void testStartSessionNoQuestions() {
        QuizConfig emptyConfig = new QuizConfig(new ArrayList<>());
        configEntity.setConfigData(emptyConfig);
        UUID configId = configEntity.getConfigId();
        String configReference = configId.toString();
        UUID userId = UUID.randomUUID();
        UUID gameId = UUID.randomUUID();
        String externalSessionReference = "extSession";
        when(quizConfigRepository.findById(configId)).thenReturn(Optional.of(configEntity));
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                quizGameService.startSession(configReference, userId, gameId, externalSessionReference));
        assertEquals("No questions found in configuration", exception.getMessage());
    }

    @Test
    void testFinishSessionSuccess() {
        UUID userId = UUID.randomUUID();
        UUID gameId = UUID.randomUUID();
        QuizGameSession session = new QuizGameSession();
        session.setTotalPoints(25);
        when(quizGameSessionRepository.findTopByUserIdAndGameIdOrderByCreatedAtDesc(userId, gameId))
                .thenReturn(Optional.of(session));
        int totalPoints = quizGameService.finishSession(userId, gameId);
        assertEquals(25, totalPoints);
    }

    @Test
    void testFinishSessionNotFound() {
        UUID userId = UUID.randomUUID();
        UUID gameId = UUID.randomUUID();
        when(quizGameSessionRepository.findTopByUserIdAndGameIdOrderByCreatedAtDesc(userId, gameId))
                .thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                quizGameService.finishSession(userId, gameId));
        assertEquals("Quiz session not found", exception.getMessage());
    }

    @Test
    void testAnswerQuestionNoMoreQuestions() {
        UUID sessionId = UUID.randomUUID();
        QuizGameSession session = new QuizGameSession();
        session.setSessionId(sessionId);
        session.setCurrentQuestion(2);
        session.setTotalPoints(0);
        session.setConfigId(configEntity.getConfigId());
        when(quizGameSessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(quizConfigRepository.findById(configEntity.getConfigId())).thenReturn(Optional.of(configEntity));
        AnswerResponse response = quizGameService.answerQuestion(sessionId, new AnswerRequest("AnyAnswer"));
        assertEquals("There are no more questions", response.getResult());
        assertEquals(0, response.getPointsAppended());
        assertEquals(session.getCurrentQuestion(), response.getCurrentQuestion());
    }

    @Test
    void testAnswerQuestionCorrect() {
        UUID sessionId = UUID.randomUUID();
        QuizGameSession session = new QuizGameSession();
        session.setSessionId(sessionId);
        session.setCurrentQuestion(1);
        session.setTotalPoints(0);
        session.setConfigId(configEntity.getConfigId());
        when(quizGameSessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(quizConfigRepository.findById(configEntity.getConfigId())).thenReturn(Optional.of(configEntity));
        when(quizGameSessionRepository.save(any(QuizGameSession.class))).thenAnswer(invocation -> invocation.getArgument(0));
        AnswerRequest answerRequest = new AnswerRequest("Paris");
        AnswerResponse response = quizGameService.answerQuestion(sessionId, answerRequest);
        assertEquals("Correct", response.getResult());
        assertEquals(10, response.getPointsAppended());
        assertEquals(1, response.getCurrentQuestion());
        assertEquals(10, response.getTotalPoints());
    }

    @Test
    void testAnswerQuestionIncorrect() {
        UUID sessionId = UUID.randomUUID();
        QuizGameSession session = new QuizGameSession();
        session.setSessionId(sessionId);
        session.setCurrentQuestion(1);
        session.setTotalPoints(0);
        session.setConfigId(configEntity.getConfigId());
        when(quizGameSessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(quizConfigRepository.findById(configEntity.getConfigId())).thenReturn(Optional.of(configEntity));
        when(quizGameSessionRepository.save(any(QuizGameSession.class))).thenAnswer(invocation -> invocation.getArgument(0));
        AnswerRequest answerRequest = new AnswerRequest("London");
        AnswerResponse response = quizGameService.answerQuestion(sessionId, answerRequest);
        assertEquals("Uncorrect", response.getResult());
        assertEquals(0, response.getPointsAppended());
        assertEquals(1, response.getCurrentQuestion());
        assertEquals(0, response.getTotalPoints());
    }

    @Test
    void testGetQuestionSuccess() {
        UUID sessionId = UUID.randomUUID();
        QuizGameSession session = new QuizGameSession();
        session.setSessionId(sessionId);
        session.setCurrentQuestion(1);
        session.setTotalPoints(5);
        session.setConfigId(configEntity.getConfigId());
        when(quizGameSessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(quizConfigRepository.findById(configEntity.getConfigId())).thenReturn(Optional.of(configEntity));
        QuestionResponse response = quizGameService.getQuestion(sessionId);
        assertEquals(1, response.getCurrentQuestion());
        assertEquals(5, response.getTotalPoints());
        @SuppressWarnings("unchecked")
        Map<String, Object> questionData = (Map<String, Object>) response.getQuestion();
        assertEquals("What is the capital of France?", questionData.get("questionText"));
        assertEquals(10, questionData.get("points"));
    }

    @Test
    void testGetQuestionNoMoreQuestions() {
        UUID sessionId = UUID.randomUUID();
        QuizGameSession session = new QuizGameSession();
        session.setSessionId(sessionId);
        session.setCurrentQuestion(2);
        session.setTotalPoints(20);
        session.setConfigId(configEntity.getConfigId());
        when(quizGameSessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(quizConfigRepository.findById(configEntity.getConfigId())).thenReturn(Optional.of(configEntity));
        QuestionResponse response = quizGameService.getQuestion(sessionId);
        assertEquals(2, response.getCurrentQuestion());
        assertEquals(20, response.getTotalPoints());
        assertEquals("No more questions", response.getQuestion());
        assertEquals(0, response.getPoints());
    }
}
