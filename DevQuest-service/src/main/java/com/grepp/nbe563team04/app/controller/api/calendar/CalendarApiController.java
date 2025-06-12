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


        Map<Long, String> colorMap = new HashMap<>();
        String[] colors = {"#F94144", "#F3722C", "#F9C74F", "#90BE6D", "#577590"};
        int colorIndex = 0;

        List<Map<String, Object>> events = new ArrayList<>();
        for (GoalResponseDto goal : goals) {
            colorMap.putIfAbsent(goal.getGoalId(), colors[colorIndex % colors.length]);
            String color = colorMap.get(goal.getGoalId());

            List<TodoResponseDto> todos = todoService.getByGoal(goal.getGoalId());
            for (TodoResponseDto todo : todos) {
                Map<String, Object> event = new HashMap<>();
                event.put("title", todo.getContent());
                event.put("start", todo.getStartDate());
                event.put("end", todo.getEndDate());
                event.put("color", color);
                event.put("url", todo.getUrl());

                if (todo.getIsDone()) {
                    event.put("color", "#d3d3d3");               // 회색 배경
                    event.put("className", "todo-done");         // 커스텀 CSS 클래스
                }

                events.add(event);
            }
            colorIndex++;
        }
        return events; //Map 에 담아 json 형태로 넘기기
    }
}

