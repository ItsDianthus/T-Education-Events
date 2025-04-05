package com.t_educational.t_edu_events.service;

import com.t_educational.t_edu_events.dto.UserProfileUpdateDto;
import com.t_educational.t_edu_events.exception.UserNotFoundException;
import com.t_educational.t_edu_events.model.account.User;
import com.t_educational.t_edu_events.model.account.UserProfile;
import com.t_educational.t_edu_events.repository.UserProfileRepository;
import com.t_educational.t_edu_events.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    public UserProfile getProfileByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с email " + email + " не найден"));
        UserProfile profile = user.getUserProfile();
        if (profile == null) {
            throw new UserNotFoundException("Профиль не найден для пользователя с email " + email);
        }
        return profile;
    }

    public UserProfile updateProfile(String email, UserProfileUpdateDto dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с email " + email + " не найден"));
        UserProfile profile = user.getUserProfile();
        if (profile == null) {
            throw new UserNotFoundException("Профиль не найден для пользователя с email " + email);
        }

        profile.setFirstName(dto.getFirstName());
        profile.setLastName(dto.getLastName());
        profile.setMiddleName(dto.getMiddleName());

        String cleanedPhone = dto.getPhone() != null ? dto.getPhone().replaceAll("\\s+", "") : null;
        profile.setPhone(cleanedPhone);

        profile.setPhotoUrl(dto.getPhotoUrl());
        profile.setResumeUrl(dto.getResumeUrl());
        profile.setSocialNick(dto.getSocialNick());

        return userProfileRepository.save(profile);
    }
}
