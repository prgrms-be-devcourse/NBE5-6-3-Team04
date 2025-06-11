package com.grepp.nbe562team04.model.todo.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodoRequestDto {

    private Long goalId;           // 어떤 목표(goal)에 속하는 투두인지
    private String content;        // 투두 내용
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isDone;        // 기본값 false로 서버에서 처리 가능
}