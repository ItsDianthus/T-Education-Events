package com.t_educational.t_edu_events.model.account;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    @Id
    @Column(name = "user_id", columnDefinition = "uuid")
    private UUID userId;

    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(name = "middle_name", length = 100)
    private String middleName;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "photo_url", length = 255)
    private String photoUrl;

    @Column(name = "resume_url", length = 255)
    private String resumeUrl;

    @Column(name = "telegram_tag", length = 100)
    private String socialNick;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    private User user;
}
