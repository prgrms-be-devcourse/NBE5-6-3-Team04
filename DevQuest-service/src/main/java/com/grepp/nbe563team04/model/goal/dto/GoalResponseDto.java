package com.grepp.nbe563team04.model.goal.dto;


import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoalResponseDto {

    private Long goalId;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isDone;
    private LocalDate createdAt;
    private String status;
    private int progress;       // 목표별 투두 진행률
    private String color;
    private String categoryName;
    private String koreanName;

    private String goalListLabel; // goalListLabel 라벨을 한글로 변환
}
