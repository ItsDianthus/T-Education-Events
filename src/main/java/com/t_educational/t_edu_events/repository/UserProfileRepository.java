package com.t_educational.t_edu_events.repository;

import com.t_educational.t_edu_events.model.account.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
}