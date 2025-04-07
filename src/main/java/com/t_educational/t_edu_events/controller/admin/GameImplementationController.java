package com.t_educational.t_edu_events.controller.admin;

import com.t_educational.t_edu_events.model.game.GameImplementation;
import com.t_educational.t_edu_events.repository.GameImplementationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/game-implementations")
@RequiredArgsConstructor
public class GameImplementationController {

    private final GameImplementationRepository gameImplementationRepository;

    // POST /api/admin/game-implementations — создание новой игровой реализации.
    @PostMapping
    public ResponseEntity<GameImplementation> createGameImplementation(@RequestBody GameImplementation gameImplementation) {
        // Доп проверки надо
        // что engineType не пустой, configReference валиден
        // TODO: Сделать проверку на существование конфига в сервисе engineType (соответствующего движка игры)
        GameImplementation saved = gameImplementationRepository.save(gameImplementation);
        return ResponseEntity.ok(saved);
    }

    // DELETE /api/admin/game-implementations/{gameId} — удаление игровой реализации.
    @DeleteMapping("/{gameId}")
    public ResponseEntity<Object> deleteGameImplementation(@PathVariable("gameId") UUID gameId) {
        return gameImplementationRepository.findById(gameId)
                .map(game -> {
                    gameImplementationRepository.delete(game);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
