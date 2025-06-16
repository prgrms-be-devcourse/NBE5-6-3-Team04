package com.grepp.nbe563team04.model.todo.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodoRequestDto {

    private Long goalId;           // 어떤 목표(goal)에 속하는 투두인지
    private String content;        // 투두 내용
    private String url;             // url
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isDone;        // 기본값 false로 서버에서 처리 가능
    private List<Long> problemIds;  // 선택된 문제 ID 목록
    private String sourceType;      // 예: "PROBLEM_RECOMMEND"
}