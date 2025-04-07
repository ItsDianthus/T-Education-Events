package com.t_educational.t_edu_events.game.quiz.controller;

import com.t_educational.t_edu_events.game.quiz.dto.AnswerRequest;
import com.t_educational.t_edu_events.game.quiz.dto.AnswerResponse;
import com.t_educational.t_edu_events.game.quiz.QuizGameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/quiz/{gameId}/session")
@RequiredArgsConstructor
public class QuizGameController {

    private final QuizGameService quizGameService;


     // POST /api/quiz/{gameId}/session/{sessionId}/answer
     @PostMapping("/{sessionId}/answer")
    public ResponseEntity<AnswerResponse> answerQuestion(
            @PathVariable UUID gameId,
            @PathVariable UUID sessionId,
            @RequestBody AnswerRequest answerRequest) {
        AnswerResponse response = quizGameService.answerQuestion(sessionId, answerRequest);
        return ResponseEntity.ok(response);
    }


     // GET /api/quiz/{gameId}/session/{sessionId}/question - возвращает данные текущего вопроса для сессии.
    @GetMapping("/{sessionId}/question")
    public ResponseEntity<?> getCurrentQuestion(
            @PathVariable UUID gameId,
            @PathVariable UUID sessionId) {
        Object questionData = quizGameService.getQuestion(sessionId);
        return ResponseEntity.ok(questionData);
    }
}
