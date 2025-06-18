package com.grepp.nbe563team04.app.model.achievement.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // 기본 생성자
public class AchievementDto {
    private String name;
    private String description;
    private String imageUrl;
    private Boolean achieved;

    public AchievementDto(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public AchievementDto(String name, String description, boolean achieved) {
        this.name = name;
        this.description = description;
        this.achieved = achieved;
    }

    public AchievementDto(String name, String description, String imageUrl, Boolean achieved) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.achieved = achieved;
    }
}