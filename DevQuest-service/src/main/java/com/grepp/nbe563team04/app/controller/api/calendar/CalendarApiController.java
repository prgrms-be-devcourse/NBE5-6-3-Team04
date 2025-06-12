package com.grepp.nbe563team04.app.controller.api.calendar;

import com.grepp.nbe563team04.model.goal.GoalService;
import com.grepp.nbe563team04.model.goal.dto.GoalResponseDto;
import com.grepp.nbe563team04.model.todo.TodoService;
import com.grepp.nbe563team04.model.todo.dto.TodoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/companies/{companyId}/events")
@RequiredArgsConstructor
public class CalendarApiController {

    private final GoalService goalService;
    private final TodoService todoService;

    @GetMapping
    public List<Map<String, Object>> getTodosByCompany(@PathVariable Long companyId) {
        List<GoalResponseDto> goals = goalService.getGoalsByCompanyId(companyId);


        List<Map<String, Object>> events = new ArrayList<>();
        for (GoalResponseDto goal : goals) {


            List<TodoResponseDto> todos = todoService.getByGoal(goal.getGoalId());
            for (TodoResponseDto todo : todos) {
                Map<String, Object> event = new HashMap<>();
                event.put("title", todo.getContent());
                event.put("start", todo.getStartDate());
                event.put("end", todo.getEndDate());
                event.put("color", goal.getColor());
                event.put("url", todo.getUrl());


                events.add(event);
            }

        }
        return events; //Map 에 담아 json 형태로 넘기기
    }
}

