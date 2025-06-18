package com.grepp.nbe563team04.app.model.userProblemSolve.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserProblemSolveRequestDto {
    private List<Long> problemIds;
}
