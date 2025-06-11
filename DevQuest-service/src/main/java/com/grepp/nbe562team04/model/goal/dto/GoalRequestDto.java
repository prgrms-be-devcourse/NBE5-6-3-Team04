package com.grepp.nbe562team04.model.goal.dto;

import lombok.*;

import java.time.LocalDate;

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
}