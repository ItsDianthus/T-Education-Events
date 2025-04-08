package com.t_educational.t_edu_events.repository;

import com.t_educational.t_edu_events.model.EventCategoryMerch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventCategoryMerchRepository extends JpaRepository<EventCategoryMerch, UUID> {
    List<EventCategoryMerch> findByEventIdAndCategoryId(UUID eventId, UUID categoryId);
}
