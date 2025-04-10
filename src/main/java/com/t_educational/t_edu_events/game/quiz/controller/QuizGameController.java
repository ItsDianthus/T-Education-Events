package com.t_educational.t_edu_events.game.quiz.controller;

import com.t_educational.t_edu_events.game.quiz.dto.AnswerRequest;
import com.t_educational.t_edu_events.game.quiz.dto.AnswerResponse;
import com.t_educational.t_edu_events.game.quiz.service.QuizGameService;
import com.t_educational.t_edu_events.game.quiz.model.QuizGameSession;
import com.t_educational.t_edu_events.game.quiz.repository.QuizGameSessionRepository;
import com.t_educational.t_edu_events.repository.account.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuizGameController {

    private final QuizGameService quizGameService;
    private final QuizGameSessionRepository quizGameSessionRepository;
    private final UserRepository userRepository;

    // GET /api/quiz/{gameId}/session – возвращает ID последней активной сессии для текущего пользователя и указанной игры
    @GetMapping("/{gameId}/session")
    public ResponseEntity<?> getActiveSession(@PathVariable UUID gameId, Principal principal) {
        String email = principal.getName();
        UUID userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found")).getId();

        Optional<QuizGameSession> sessionOpt = quizGameSessionRepository
                .findTopByUserIdAndGameIdOrderByCreatedAtDesc(userId, gameId);
        if (sessionOpt.isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("sessionId", sessionOpt.get().getSessionId().toString());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Active session not found");
        }
    }

    // POST /api/quiz/{gameId}/answer – принимает ответ для последней активной сессии по указанной игре
    @PostMapping("/{gameId}/answer")
    public ResponseEntity<AnswerResponse> answerQuestion(
            @PathVariable UUID gameId,
            @RequestBody AnswerRequest answerRequest,
            Principal principal) {
        String email = principal.getName();
        UUID userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found")).getId();

        // Находим последнюю активную сессию для данного пользователя и игры (это важноо)
        QuizGameSession session = quizGameSessionRepository
                .findTopByUserIdAndGameIdOrderByCreatedAtDesc(userId, gameId)
                .orElseThrow(() -> new RuntimeException("Active session not found"));

        AnswerResponse response = quizGameService.answerQuestion(session.getSessionId(), answerRequest);
        return ResponseEntity.ok(response);
    }

    // GET /api/quiz/{gameId}/question – возвращает данные текущего вопроса для последней активной сессии по указанной игре
    @GetMapping("/{gameId}/question")
    public ResponseEntity<?> getCurrentQuestion(@PathVariable UUID gameId, Principal principal) {
        String email = principal.getName();
        UUID userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found")).getId();

        QuizGameSession session = quizGameSessionRepository
                .findTopByUserIdAndGameIdOrderByCreatedAtDesc(userId, gameId)
                .orElseThrow(() -> new RuntimeException("Active session not found"));

        Object questionData = quizGameService.getQuestion(session.getSessionId());
        return ResponseEntity.ok(questionData);
    }
}
