package com.grepp.nbe563team04.app.model.todo.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TodoResponseDto {

    private Long todoId;
    private String content;
    private String url;             // url
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isDone;
    private Long goalId;
}