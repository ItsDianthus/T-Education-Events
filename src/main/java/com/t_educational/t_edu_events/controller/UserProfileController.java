package com.t_educational.t_edu_events.controller;

import com.t_educational.t_edu_events.dto.UserProfileUpdateDto;
import com.t_educational.t_edu_events.model.account.UserProfile;
import com.t_educational.t_edu_events.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/user/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    // GET /api/user/profile — получение информации о профиле пользователя
    @GetMapping
    public ResponseEntity<UserProfile> getUserProfile(Principal principal) {
        String email = principal.getName();
        UserProfile profile = userProfileService.getProfileByEmail(email);
        return ResponseEntity.ok(profile);
    }

    // PUT /api/user/profile — обновление информации о профиле пользователя
    @PutMapping
    public ResponseEntity<UserProfile> updateUserProfile(@Valid @RequestBody UserProfileUpdateDto updatedProfileDto,
                                                         Principal principal) {
        String email = principal.getName();
        UserProfile profile = userProfileService.updateProfile(email, updatedProfileDto);
        return ResponseEntity.ok(profile);
    }
}
