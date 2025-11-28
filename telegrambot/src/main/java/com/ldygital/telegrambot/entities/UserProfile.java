package com.ldygital.telegrambot.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.Instant;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserProfile {
    @Id
    private String userId;
    private String level = "A1";
    private int experiencePoints = 0;
}
