package com.grepp.nbe563team04.model.problem.dto;

import com.grepp.nbe563team04.model.goalcompany.code.GoalStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
public class ProblemRequestDto {
    private List<Long> problemIds;
    private Long goalId;
}
