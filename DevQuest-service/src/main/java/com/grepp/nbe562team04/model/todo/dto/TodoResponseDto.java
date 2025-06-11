package com.grepp.nbe562team04.model.todo.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TodoResponseDto {

    private Long todoId;
    private String content;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isDone;
    private Long goalId;
}