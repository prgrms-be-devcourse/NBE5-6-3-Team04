package com.grepp.nbe563team04.app.model.problem.dto;

import lombok.*;

import java.util.List;

@Getter @Setter
public class ProblemRequestDto {
    private List<Long> problemIds;
    private Long goalId;
}
