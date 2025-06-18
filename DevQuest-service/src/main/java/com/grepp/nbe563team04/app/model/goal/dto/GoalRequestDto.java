package com.grepp.nbe563team04.app.model.goal.dto;


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
public class GoalRequestDto {

    private Long companyId; // 어떤 기업의 목표인지 지정
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isDone;
    private String color;
    private String categoryName;
}