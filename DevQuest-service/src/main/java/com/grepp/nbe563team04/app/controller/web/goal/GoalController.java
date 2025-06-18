package com.grepp.nbe563team04.app.controller.web.goal;

import com.grepp.nbe563team04.model.auth.domain.Principal;
import com.grepp.nbe563team04.model.dashboard.DashboardService;
import com.grepp.nbe563team04.model.dashboard.dto.DashboardDto;
import com.grepp.nbe563team04.model.goal.GoalService;
import com.grepp.nbe563team04.model.goal.dto.GoalResponseDto;
import com.grepp.nbe563team04.model.goalCategory.GoalCategoryService;
import com.grepp.nbe563team04.model.goalCategory.dto.GoalCategoryResponseDto;
import com.grepp.nbe563team04.model.goalcompany.GoalCompanyService;
import com.grepp.nbe563team04.model.goalcompany.dto.GoalCompanyResponseDto;
import com.grepp.nbe563team04.model.member.MemberRepository;
import com.grepp.nbe563team04.model.member.entity.Member;
import com.grepp.nbe563team04.model.todo.TodoService;
import com.grepp.nbe563team04.model.todo.dto.TodoResponseDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class GoalController {

    // 생성자 주입

    // Repository 주입
    private final MemberRepository memberRepository;

    // Service 주입
    private final DashboardService dashboardService;
    private final GoalCompanyService goalCompanyService;
    private final GoalService goalService;
    private final TodoService todoService;
    private final GoalCategoryService goalCategoryService;


    // 목표기업 단일 조회(진행률 조회 포함)
    @GetMapping("/companies/{companyId}/select")
    public String companyDetail(@PathVariable Long companyId, Model model, @AuthenticationPrincipal Principal principal) {
        Member detachedMember = principal.getMember();

        Member managedMember = memberRepository.findById(detachedMember.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        DashboardDto dashboardDto = dashboardService.getDashboard(managedMember);
        GoalCompanyResponseDto goalCompanyDto = goalCompanyService.getGoalCompanyById(companyId);
        List<GoalResponseDto> goalDtos = goalService.getGoalsByCompanyId(companyId); // 기업별 목표 리스트 DTO 리스트 받아오기
        List<GoalCategoryResponseDto> goalCategoryDtos = goalCategoryService.getGoalCategories();

        // 목표별 투두 리스트 맵으로 저장
        Map<Long, List<TodoResponseDto>> todoMap = new HashMap<>(); // goalId, 투두 리스트 로 저장

        for (GoalResponseDto goalDto : goalDtos) {
            List<TodoResponseDto> todos = todoService.getTodosByGoal(goalDto.getGoalId());
            todoMap.put(goalDto.getGoalId(), todos);
        }

        model.addAttribute("dashboard", dashboardDto);  // 대시보드 정보
        model.addAttribute("company", goalCompanyDto);      // 기업 정보
        model.addAttribute("goals", goalDtos);          // 목표 리스트(진행률 포함)
        model.addAttribute("todoMap", todoMap);
        model.addAttribute("categories", goalCategoryDtos);


        return "goal/goal";
    }




}

