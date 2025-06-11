package com.grepp.nbe562team04.app.controller.api.todo;

import com.grepp.nbe562team04.model.achievement.AchievementService;
import com.grepp.nbe562team04.model.auth.domain.Principal;
import com.grepp.nbe562team04.model.todo.dto.TodoRequestDto;
import com.grepp.nbe562team04.model.todo.dto.TodoResponseDto;
import com.grepp.nbe562team04.model.todo.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/todos")
public class TodoApiController {

    private final TodoService todoService;
    private final AchievementService achievementService;

    //  투두 생성
    @PostMapping("/{goalId}/create")
    public ResponseEntity<String> createTodo(@RequestBody TodoRequestDto dto) {
        todoService.create(dto);
        return ResponseEntity.ok("투두 생성 완료!");
    }

    //  특정 목표에 속한 투두 목록 조회
    @GetMapping("/{goalId}")
    public ResponseEntity<List<TodoResponseDto>> getTodosByGoal(@PathVariable Long goalId) {
        List<TodoResponseDto> todoList = todoService.getByGoal(goalId);
        return ResponseEntity.ok(todoList);
    }

    // 단일 투두 조회
    @GetMapping("/{todoId}/select")
    public ResponseEntity<TodoResponseDto> getTodo(@PathVariable Long todoId) {
        TodoResponseDto todo = todoService.getById(todoId);
        return ResponseEntity.ok(todo);
    }

    //  투두 수정
    @PutMapping("/{todoId}/update")
    public ResponseEntity<String> updateTodo(@PathVariable Long todoId, @RequestBody TodoRequestDto dto) {
        todoService.update(todoId, dto);
        return ResponseEntity.ok("투두 수정 완료!");
    }

    //  투두 삭제
    @DeleteMapping("/{todoId}/delete")
    public ResponseEntity<String> deleteTodo(@PathVariable Long todoId) {
        todoService.delete(todoId);
        return ResponseEntity.ok("투두 삭제 완료!");
    }

    @PutMapping("/{todoId}/toggle")
    public ResponseEntity<String> toggleTodoStatus(@PathVariable Long todoId, @RequestBody Map<String, Object> request) {
        Boolean isDone = (Boolean) request.get("isDone");
        todoService.toggleStatus(todoId, isDone);
        return ResponseEntity.ok("상태 업데이트 완료");
    }

    @PostMapping("/{todoId}/toggle-check")
    public ResponseEntity<Map<String, String>> toggleTodoCheck(@PathVariable Long todoId,
                                                               @AuthenticationPrincipal Principal principal) {
        todoService.toggleCheck(todoId, principal.getUser());

        Map<String, String> response = new HashMap<>();
        // 업적 부여 결과 체크
        String firstAchievement = achievementService.giveTodoFirstCheckAchievement(principal.getUser().getUserId());
        if (firstAchievement != null) {
            response.put("achievementName", firstAchievement);
        }

        String fiveDoneAchievement = achievementService.giveTodoFiveCheckAchievement(principal.getUser().getUserId());
        if (fiveDoneAchievement != null) {
            response.put("achievementName", fiveDoneAchievement); // 덮어씀 → 마지막 업적 모달 표시
        }

        return ResponseEntity.ok(response);
    }
}