package com.grepp.nbe562team04.app.controller.web.goal;

import com.grepp.nbe562team04.model.auth.domain.Principal;
import com.grepp.nbe562team04.model.dashboard.DashboardService;
import com.grepp.nbe562team04.model.dashboard.dto.DashboardDto;
import com.grepp.nbe562team04.model.dashboard.dto.GoalCompanyDto;
import com.grepp.nbe562team04.model.goal.GoalService;
import com.grepp.nbe562team04.model.goal.dto.GoalResponseDto;
import com.grepp.nbe562team04.model.todo.TodoService;
import com.grepp.nbe562team04.model.todo.dto.TodoResponseDto;
import com.grepp.nbe562team04.model.user.UserRepository;
import com.grepp.nbe562team04.model.user.entity.User;
import java.util.Comparator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class GoalController {

    private final DashboardService dashboardService;
    private final UserRepository userRepository;
    private final GoalService goalService;
    private final TodoService todoService;

    // 목표기업 단일 조회(진행률 조회 포함)
    @GetMapping("/companies/{companyId}/select")
    public String companyDetail(@AuthenticationPrincipal Principal principal,@PathVariable Long companyId, Model model) {
        User detachedUser = principal.getUser();

        User managedUser = userRepository.findById(detachedUser.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        DashboardDto dto = dashboardService.getDashboard(managedUser);
        GoalCompanyDto companyDto = dashboardService.getCompanyDetailById(companyId);
        List<GoalResponseDto> goalList = goalService.getGoalsByCompanyId(companyId);


        Map<Long, List<TodoResponseDto>> todoMap = new HashMap<>(); // 목표별 투두 리스트 맵으로 저장

        for (GoalResponseDto goal : goalList) {
            List<TodoResponseDto> todos = todoService.getByGoal(goal.getGoalId());
            todoMap.put(goal.getGoalId(), todos);
        }

        model.addAttribute("dashboard", dto);
        model.addAttribute("company", companyDto);   // 기업 정보
        model.addAttribute("goals", goalList);       // 목표 리스트(진행률 포함)
        model.addAttribute("todoMap", todoMap);

        return "goal/goal";
    }
}

