package com.grepp.nbe563team04.model.problem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProblemResponseDto {
    private Long problemId;
    private String title;
    private Integer level;
    private String url;
    private String site;
//    private int solvedCount;
}

