package com.grepp.nbe563team04.app.model.goalCategory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class GoalCategoryResponseDto {
    private Long categoryId;
    private String categoryName;
    private String color;
    private String koreanName;
}
