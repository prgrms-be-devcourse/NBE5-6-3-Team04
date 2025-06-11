package com.grepp.nbe562team04.model.achievement.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // 기본 생성자
public class AchievementDto {
    private String name;
    private String description;

    public AchievementDto(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getters (Lombok 써도 OK)
}