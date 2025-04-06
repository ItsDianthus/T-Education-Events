package com.t_educational.t_edu_events.repository;

import com.t_educational.t_edu_events.model.EventCategoryGame;
import com.t_educational.t_edu_events.model.EventCategoryGameId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EventCategoryGameRepository extends JpaRepository<EventCategoryGame, EventCategoryGameId> {
    List<EventCategoryGame> findAllByIdEventIdAndIdCategoryId(UUID eventId, UUID categoryId);
}
