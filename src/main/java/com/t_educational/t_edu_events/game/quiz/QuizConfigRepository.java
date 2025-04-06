package com.t_educational.t_edu_events.game.quiz;

import com.t_educational.t_edu_events.game.quiz.QuizConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface QuizConfigRepository extends JpaRepository<QuizConfigEntity, UUID> {
}
