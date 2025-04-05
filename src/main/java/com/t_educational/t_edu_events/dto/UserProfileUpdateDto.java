package com.t_educational.t_edu_events.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserProfileUpdateDto {

    @NotBlank(message = "Имя не должно быть пустым")
    @Size(max = 100, message = "Имя не может превышать 100 символов")
    @Pattern(regexp = "^[\\p{L}]+$", message = "Имя может содержать только буквы")
    private String firstName;

    @NotBlank(message = "Фамилия не должна быть пустой")
    @Size(max = 100, message = "Фамилия не может превышать 100 символов")
    @Pattern(regexp = "^[\\p{L}]+$", message = "Фамилия может содержать только буквы")
    private String lastName;

    @Size(max = 100, message = "Отчество не может превышать 100 символов")
    @Pattern(regexp = "^[\\p{L}]+$", message = "Отчество может содержать только буквы")
    private String middleName;

    @Pattern(
            regexp = "^(?:\\+7\\d{10}|\\+7\\s\\d{3}\\s\\d{3}\\s\\d{2}\\s\\d{2})$",
            message = "Телефон должен быть в формате +7xxxxxxxxxx или +7 xxx xxx xx xx"
    )
    private String phone;

    @Pattern(
            regexp = "^(https?://).+\\.(?i)(jpg|jpeg|pdf)$",
            message = "Ссылка должна быть корректным URL и оканчиваться на .jpg, .jpeg или .pdf"
    )
    @Size(max = 255, message = "Ссылка на фотографию не может превышать 255 символов")
    private String photoUrl;

    @Pattern(
            regexp = "^(https?://).+\\.(?i)(pdf)$",
            message = "Ссылка должна быть корректным URL и оканчиваться на .pdf"
    )
    @Size(max = 255, message = "Ссылка на фотографию не может превышать 255 символов")
    private String resumeUrl;

    @Size(min = 5, max = 24, message = "Telegram tag должен содержать от 5 до 24 символов")
    @Pattern(
            regexp = "^@[A-Za-z0-9_]+$",
            message = "Telegram tag должен начинаться с @ и состоять только из латинских букв, цифр и нижнего подчеркивания"
    )
    private String socialNick;
}
