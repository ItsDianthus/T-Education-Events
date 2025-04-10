package com.t_educational.t_edu_events.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.t_educational.t_edu_events.dto.UserProfileUpdateDto;
import com.t_educational.t_edu_events.exception.UserNotFoundException;
import com.t_educational.t_edu_events.model.account.User;
import com.t_educational.t_edu_events.model.account.UserProfile;
import com.t_educational.t_edu_events.repository.account.UserProfileRepository;
import com.t_educational.t_edu_events.repository.account.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UserProfileServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private UserProfileService userProfileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetProfileByEmailSuccess() {
        String email = "test@example.com";
        User user = new User();
        UserProfile profile = new UserProfile();
        user.setUserProfile(profile);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserProfile result = userProfileService.getProfileByEmail(email);
        assertEquals(profile, result);
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testGetProfileByEmailUserNotFound() {
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class,
                () -> userProfileService.getProfileByEmail(email));
        assertEquals("Пользователь с email " + email + " не найден", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testGetProfileByEmailProfileNotFound() {
        String email = "test@example.com";
        User user = new User();
        user.setUserProfile(null);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Exception exception = assertThrows(UserNotFoundException.class,
                () -> userProfileService.getProfileByEmail(email));
        assertEquals("Профиль не найден для пользователя с email " + email, exception.getMessage());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testUpdateProfileSuccess() {
        String email = "test@example.com";
        User user = new User();
        UserProfile profile = new UserProfile();
        user.setUserProfile(profile);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserProfileUpdateDto dto = new UserProfileUpdateDto();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setMiddleName("Michael");
        dto.setPhone(" 123 456 7890 ");
        dto.setPhotoUrl("http://example.com/photo.jpg");
        dto.setResumeUrl("http://example.com/resume.pdf");
        dto.setSocialNick("john_doe");

        when(userProfileRepository.save(any(UserProfile.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserProfile updatedProfile = userProfileService.updateProfile(email, dto);
        assertEquals("John", updatedProfile.getFirstName());
        assertEquals("Doe", updatedProfile.getLastName());
        assertEquals("Michael", updatedProfile.getMiddleName());
        assertEquals("1234567890", updatedProfile.getPhone());
        assertEquals("http://example.com/photo.jpg", updatedProfile.getPhotoUrl());
        assertEquals("http://example.com/resume.pdf", updatedProfile.getResumeUrl());
        assertEquals("john_doe", updatedProfile.getSocialNick());
        verify(userRepository, times(1)).findByEmail(email);
        verify(userProfileRepository, times(1)).save(profile);
    }

    @Test
    void testUpdateProfileUserNotFound() {
        String email = "nonexistent@example.com";
        UserProfileUpdateDto dto = new UserProfileUpdateDto();
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class,
                () -> userProfileService.updateProfile(email, dto));
        assertEquals("Пользователь с email " + email + " не найден", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testUpdateProfileProfileNotFound() {
        String email = "test@example.com";
        User user = new User();
        user.setUserProfile(null);
        UserProfileUpdateDto dto = new UserProfileUpdateDto();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Exception exception = assertThrows(UserNotFoundException.class,
                () -> userProfileService.updateProfile(email, dto));
        assertEquals("Профиль не найден для пользователя с email " + email, exception.getMessage());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testUpdateProfileNullPhone() {
        String email = "test@example.com";
        User user = new User();
        UserProfile profile = new UserProfile();
        user.setUserProfile(profile);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserProfileUpdateDto dto = new UserProfileUpdateDto();
        dto.setFirstName("Alice");
        dto.setLastName("Smith");
        dto.setMiddleName("Jane");
        dto.setPhone(null);
        dto.setPhotoUrl("http://example.com/photo.png");
        dto.setResumeUrl("http://example.com/resume.doc");
        dto.setSocialNick("alice_s");

        when(userProfileRepository.save(any(UserProfile.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserProfile updatedProfile = userProfileService.updateProfile(email, dto);
        assertEquals("Alice", updatedProfile.getFirstName());
        assertEquals("Smith", updatedProfile.getLastName());
        assertEquals("Jane", updatedProfile.getMiddleName());
        assertNull(updatedProfile.getPhone());
        assertEquals("http://example.com/photo.png", updatedProfile.getPhotoUrl());
        assertEquals("http://example.com/resume.doc", updatedProfile.getResumeUrl());
        assertEquals("alice_s", updatedProfile.getSocialNick());
        verify(userRepository, times(1)).findByEmail(email);
        verify(userProfileRepository, times(1)).save(profile);
    }

    @Test
    void testUpdateProfilePhoneWithOnlySpaces() {
        String email = "test@example.com";
        User user = new User();
        UserProfile profile = new UserProfile();
        user.setUserProfile(profile);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserProfileUpdateDto dto = new UserProfileUpdateDto();
        dto.setPhone("     ");
        when(userProfileRepository.save(any(UserProfile.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserProfile updatedProfile = userProfileService.updateProfile(email, dto);
        assertEquals("", updatedProfile.getPhone());
        verify(userRepository, times(1)).findByEmail(email);
        verify(userProfileRepository, times(1)).save(profile);
    }

    @Test
    void testUpdateProfileAllNullFields() {
        String email = "test@example.com";
        User user = new User();
        UserProfile profile = new UserProfile();
        profile.setFirstName("Existing");
        profile.setLastName("User");
        profile.setMiddleName("Name");
        profile.setPhone("1234567890");
        profile.setPhotoUrl("http://old-photo.com");
        profile.setResumeUrl("http://old-resume.com");
        profile.setSocialNick("old_nick");
        user.setUserProfile(profile);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserProfileUpdateDto dto = new UserProfileUpdateDto();
        when(userProfileRepository.save(any(UserProfile.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserProfile updatedProfile = userProfileService.updateProfile(email, dto);
        assertNull(updatedProfile.getFirstName());
        assertNull(updatedProfile.getLastName());
        assertNull(updatedProfile.getMiddleName());
        assertNull(updatedProfile.getPhone());
        assertNull(updatedProfile.getPhotoUrl());
        assertNull(updatedProfile.getResumeUrl());
        assertNull(updatedProfile.getSocialNick());
        verify(userRepository, times(1)).findByEmail(email);
        verify(userProfileRepository, times(1)).save(profile);
    }
}
