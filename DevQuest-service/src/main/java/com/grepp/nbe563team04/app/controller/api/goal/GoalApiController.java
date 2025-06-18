package com.grepp.nbe563team04.app.controller.api.goal;

import com.grepp.nbe563team04.model.auth.domain.Principal;
import com.grepp.nbe563team04.model.goal.GoalService;
import com.grepp.nbe563team04.model.goal.dto.GoalRequestDto;
import com.grepp.nbe563team04.model.goal.dto.GoalResponseDto;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/goals")
public class GoalApiController {

    private final GoalService goalService;

    //  목표 생성
    @PostMapping("/{companyId}/create")
    public ResponseEntity<?> createGoal( @RequestBody GoalRequestDto dto, @AuthenticationPrincipal Principal principal) {
        Long userId = principal.getMember().getUserId();
        String achievementName = goalService.createGoal(dto, userId);// 로그인된 유저 ID 꺼내기

        if (achievementName != null) {
            return ResponseEntity.ok(Map.of(
                    "message", "등록 완료",
                    "achievementName", achievementName
            ));
        } else {
            return ResponseEntity.ok(Map.of(
                    "message", "등록 완료"
            ));
        }
    }

    //  목표 단건 조회
    @GetMapping("/{goalId}/select")
    public ResponseEntity<GoalResponseDto> selectGoal(@PathVariable Long goalId) {
        GoalResponseDto dto = goalService.getGoalById(goalId);
        return ResponseEntity.ok(dto);
    }

    //  목표 수정
    @PutMapping("/{goalId}/update")
    public ResponseEntity<String> update(@PathVariable Long goalId, @RequestBody GoalRequestDto dto) {
        goalService.updateGoal(goalId, dto);
        return ResponseEntity.ok("목표 수정 완료");
    }

    //  목표 삭제
    @DeleteMapping("/{goalId}/delete")
    public ResponseEntity<String> delete(@PathVariable Long goalId) {
        goalService.deleteGoal(goalId);
        return ResponseEntity.ok("목표 삭제 완료");
    }

    // 목표 완료
    @PostMapping("/{goalId}/complete")
    public ResponseEntity<Map<String, Object>> completeGoal(
            @PathVariable Long goalId,
            @AuthenticationPrincipal Principal principal) {

        Map<String, Object> result = goalService.completeGoal(goalId, principal.getMember());

        return ResponseEntity.ok(result);
    }
}