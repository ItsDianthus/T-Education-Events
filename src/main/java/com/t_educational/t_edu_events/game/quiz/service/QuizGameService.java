package com.t_educational.t_edu_events.game.quiz.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.t_educational.t_edu_events.game.quiz.dto.AnswerRequest;
import com.t_educational.t_edu_events.game.quiz.dto.AnswerResponse;
import com.t_educational.t_edu_events.game.quiz.dto.QuestionResponse;
import com.t_educational.t_edu_events.game.quiz.model.QuizConfig;
import com.t_educational.t_edu_events.game.quiz.model.QuizConfigEntity;
import com.t_educational.t_edu_events.game.quiz.model.QuizGameSession;
import com.t_educational.t_edu_events.game.quiz.repository.QuizConfigRepository;
import com.t_educational.t_edu_events.game.quiz.repository.QuizGameSessionRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class QuizGameService {

    private final QuizConfigRepository quizConfigRepository;
    private final QuizGameSessionRepository quizGameSessionRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public QuizGameService(QuizConfigRepository quizConfigRepository,
                           QuizGameSessionRepository quizGameSessionRepository) {
        this.quizConfigRepository = quizConfigRepository;
        this.quizGameSessionRepository = quizGameSessionRepository;
    }
    public String startSession(String configReference, UUID userId, UUID gameId, String gameSessionIdReference) {
        UUID configId = UUID.fromString(configReference);

        QuizConfigEntity configEntity = quizConfigRepository.findById(configId)
                .orElseThrow(() -> new RuntimeException("Quiz configuration not found"));
        QuizConfig config = configEntity.getConfigData();
        if (config.getQuestions() == null || config.getQuestions().isEmpty()) {
            throw new RuntimeException("No questions found in configuration");
        }

        QuizConfig.Question firstQuestion = config.getQuestions().get(0);

        Map<String, Object> questionData = new HashMap<>();
        questionData.put("questionText", firstQuestion.getQuestionText());
        questionData.put("possibleAnswers", firstQuestion.getPossibleAnswers());
        questionData.put("points", firstQuestion.getPoints());

        Map<String, Object> engineState = new HashMap<>();
        engineState.put("currentQuestion", 1);
        engineState.put("totalPoints", 0);
        engineState.put("question", questionData);

        QuizGameSession quizSession = new QuizGameSession();
        quizSession.setUserId(userId);
        quizSession.setGameId(gameId);
        quizSession.setGameSessionIdReference(gameSessionIdReference);
        quizSession.setCurrentQuestion(1);
        quizSession.setTotalPoints(0);
        quizSession.setConfigId(configId);

        QuizGameSession savedQuizSession = quizGameSessionRepository.save(quizSession);

        engineState.put("internalSessionId", savedQuizSession.getSessionId().toString());

        try {
            return objectMapper.writeValueAsString(engineState);
        } catch (Exception e) {
            throw new RuntimeException("Error generating engine data", e);
        }
    }

    public int finishSession(UUID userId, UUID gameId) {
        Optional<QuizGameSession> quizSessionOpt = quizGameSessionRepository.findTopByUserIdAndGameIdOrderByCreatedAtDesc(userId, gameId);
        if (quizSessionOpt.isEmpty()) {
            throw new RuntimeException("Quiz session not found");
        }
        QuizGameSession quizSession = quizSessionOpt.get();
        return quizSession.getTotalPoints();
    }


     //Обрабатывает ответ пользователя для внутренней QUIZ-сессии.
     //Чекает ответ текущего вопроса, обновляет сессию и возвращает результат

    public AnswerResponse answerQuestion(UUID sessionId, AnswerRequest answerRequest) {
        QuizGameSession session = quizGameSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Internal session not found"));

        UUID configId = session.getConfigId();
        QuizConfigEntity configEntity = quizConfigRepository.findById(configId)
                .orElseThrow(() -> new RuntimeException("Quiz configuration not found"));
        QuizConfig config = configEntity.getConfigData();

        int currentIndex = session.getCurrentQuestion() - 1;
        // Вопросы кончились
        if (currentIndex < 0 || currentIndex >= config.getQuestions().size()) {
            AnswerResponse response = new AnswerResponse();
            response.setResult("There are no more questions");
            response.setPointsAppended(0);
            response.setCurrentQuestion(session.getCurrentQuestion());
            response.setQuestionText(null);
            response.setPossibleAnswers(null);
            response.setQuestionPoints(0);
            response.setCorrectAnswers(null);
            response.setTotalPoints(session.getTotalPoints());
            return response;
        }

        QuizConfig.Question currentQuestion = config.getQuestions().get(currentIndex);

        boolean correct = currentQuestion.getCorrectAnswers().stream()
                .anyMatch(ans -> ans.equalsIgnoreCase(answerRequest.getAnswer()));
        int pointsAwarded = correct ? currentQuestion.getPoints() : 0;

        AnswerResponse response = new AnswerResponse();
        response.setResult(correct ? "Correct" : "Uncorrect");
        response.setPointsAppended(pointsAwarded);
        response.setCurrentQuestion(session.getCurrentQuestion()); // Номер отвеченного вопроса
        response.setQuestionText(currentQuestion.getQuestionText());
        response.setPossibleAnswers(currentQuestion.getPossibleAnswers());
        response.setQuestionPoints(currentQuestion.getPoints());
        response.setCorrectAnswers(currentQuestion.getCorrectAnswers());
        response.setTotalPoints(session.getTotalPoints() + pointsAwarded);

        session.setTotalPoints(session.getTotalPoints() + pointsAwarded);
        session.setCurrentQuestion(session.getCurrentQuestion() + 1);
        quizGameSessionRepository.save(session);

        return response;
    }

    public QuestionResponse getQuestion(UUID sessionId) {
        QuizGameSession session = quizGameSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Internal session not found"));

        UUID configId = session.getConfigId();
        QuizConfigEntity configEntity = quizConfigRepository.findById(configId)
                .orElseThrow(() -> new RuntimeException("Quiz configuration not found"));
        QuizConfig config = configEntity.getConfigData();

        int currentIndex = session.getCurrentQuestion() - 1;
        QuestionResponse questionResponse = new QuestionResponse();
        questionResponse.setCurrentQuestion(session.getCurrentQuestion());
        questionResponse.setTotalPoints(session.getTotalPoints());

        if (currentIndex < 0 || currentIndex >= config.getQuestions().size()) {
            questionResponse.setQuestion("No more questions");
            questionResponse.setPoints(0);
        } else {
            QuizConfig.Question question = config.getQuestions().get(currentIndex);
            Map<String, Object> questionData = new HashMap<>();
            questionData.put("questionText", question.getQuestionText());
            questionData.put("possibleAnswers", question.getPossibleAnswers());
            questionData.put("points", question.getPoints());

            questionResponse.setQuestion(questionData);
            questionResponse.setPoints(question.getPoints());
        }
        return questionResponse;
    }
}
