package com.t_educational.t_edu_events.repository;

import com.t_educational.t_edu_events.model.game.GameImplementation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GameImplementationRepository extends JpaRepository<GameImplementation, UUID> {
}
