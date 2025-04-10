package com.t_educational.t_edu_events.controller.admin;

import com.t_educational.t_edu_events.game.quiz.service.QuizOuterGameService;
import com.t_educational.t_edu_events.model.game.GameImplementation;
import com.t_educational.t_edu_events.repository.GameImplementationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/games")
@RequiredArgsConstructor
public class AdminGameImplementationController {

    private final GameImplementationRepository gameImplementationRepository;
    private final QuizOuterGameService quizOuterGameService;

    @PostMapping
    public ResponseEntity<GameImplementation> createGameImplementation(@RequestBody GameImplementation gameImplementation) {
        if ("QUIZ".equalsIgnoreCase(gameImplementation.getEngineType())) {
            quizOuterGameService.validateConfig(gameImplementation.getConfigReference());
            int maxPoints = quizOuterGameService.calculateMaxPoints(gameImplementation.getConfigReference());
            gameImplementation.setMaxPoints(maxPoints);
        }
        GameImplementation saved = gameImplementationRepository.save(gameImplementation);
        return ResponseEntity.ok(saved);
    }

    // DELETE /api/admin/game-implementations/{gameId} — удаление игровой реализации.
    @DeleteMapping("/{gameId}")
    public ResponseEntity<Object> deleteGameImplementation(@PathVariable UUID gameId) {
        return gameImplementationRepository.findById(gameId)
                .map(game -> {
                    gameImplementationRepository.delete(game);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // GET /api/admin/game-implementations — возвращает список всех игровых реализаций.
    @GetMapping
    public ResponseEntity<List<GameImplementation>> getAllGameImplementations() {
        List<GameImplementation> list = gameImplementationRepository.findAll();
        return ResponseEntity.ok(list);
    }

    // GET /api/admin/game-implementations/category/{categoryId} — возвращает игровые реализации, связанные с указанной категорией.
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<GameImplementation>> getGameImplementationsByCategory(@PathVariable UUID categoryId) {
        List<GameImplementation> list = gameImplementationRepository.findByCategories_Id(categoryId);
        return ResponseEntity.ok(list);
    }

    // PUT /api/admin/game-implementations/{gameId}/toggle-home - переключение домашнего режима.
    @PutMapping("/{gameId}/toggle-home")
    public ResponseEntity<GameImplementation> toggleHome(
            @PathVariable UUID gameId) {
        GameImplementation game = gameImplementationRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));
        game.setHomeEnable(!game.isHomeEnable());
        GameImplementation updated = gameImplementationRepository.save(game);
        return ResponseEntity.ok(updated);
    }
}
