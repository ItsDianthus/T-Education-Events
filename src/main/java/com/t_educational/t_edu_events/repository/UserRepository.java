package com.t_educational.t_edu_events.repository;

import com.t_educational.t_edu_events.model.account.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}