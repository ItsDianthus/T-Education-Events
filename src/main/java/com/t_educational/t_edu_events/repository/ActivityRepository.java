package com.t_educational.t_edu_events.repository;

import com.t_educational.t_edu_events.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, UUID> {
    List<Activity> findByEventId(UUID eventId);
}
