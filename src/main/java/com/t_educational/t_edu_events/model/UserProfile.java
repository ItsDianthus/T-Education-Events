package com.t_educational.t_edu_events.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName; // Имя
    private String lastName; // Фамилия
    private String patronymic; // Отчество
    private String photoUrl; // Ссылка на фото
    private String telegram; // Телеграм
    private String city; // Город
    private String phone; // Телефон
    private String employmentStatus; // Занятость (возможно перечисление)
    private String resumeUrl; // Ссылка на резюме (файл PDF)

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user; // Связь с пользователем

    // Геттеры и сеттеры
}
