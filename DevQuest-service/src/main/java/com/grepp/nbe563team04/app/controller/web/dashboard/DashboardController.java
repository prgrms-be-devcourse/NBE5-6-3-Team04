package com.grepp.nbe563team04.app.controller.web.dashboard;

import com.grepp.nbe563team04.model.auth.domain.Principal;
import com.grepp.nbe563team04.model.dashboard.DashboardRepository;
import com.grepp.nbe563team04.model.dashboard.DashboardService;
import com.grepp.nbe563team04.model.dashboard.dto.DashboardDto;
import com.grepp.nbe563team04.model.goal.GoalService;
import com.grepp.nbe563team04.model.todo.TodoService;
import com.grepp.nbe563team04.model.member.MemberImageRepository;
import com.grepp.nbe563team04.model.member.MemberRepository;
import com.grepp.nbe563team04.model.member.MemberService;
import com.grepp.nbe563team04.model.member.entity.Member;
import com.grepp.nbe563team04.model.member.entity.MemberImage;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;
    private final MemberRepository memberRepository;
    private final DashboardRepository dashboardRepository;
    private final MemberImageRepository memberImageRepository;
    private final GoalService goalService;
    private final TodoService todoService;
    private final MemberService memberService;


    public DashboardController(DashboardService dashboardService, MemberRepository memberRepository,
                               DashboardRepository dashboardRepository, MemberImageRepository memberImageRepository,   GoalService goalService, TodoService todoService,
        MemberService memberService) {

        this.dashboardService = dashboardService;
        this.memberRepository = memberRepository;
        this.dashboardRepository = dashboardRepository;
        this.memberImageRepository = memberImageRepository;
        this.goalService = goalService;
        this.todoService = todoService;
        this.memberService = memberService;
    }

    // 대시보드
    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal Principal principal, Model model, CsrfToken csrfToken) {
        Member member = memberRepository.findById(principal.getUser().getUserId())
            .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        MemberImage image = memberImageRepository.findTopByMemberAndActivatedOrderByCreatedAtDesc(member, true)
                .orElse(null);


        // 💡 member.getLevel()이 LAZY라면, 여기서 미리 호출해서 로딩해두면 좋음
        member.getLevel().getLevelName(); // 강제 초기화

        DashboardDto dto = dashboardService.getDashboard(member);

        model.addAttribute("dashboard", dto);
        model.addAttribute("member", member); // ⭐️ 유저도 모델에 포함!
        model.addAttribute("image", image);
        model.addAttribute("_csrf", csrfToken);
        return "dashboard/dashboard";
    }

    // 알림 토글
    @PostMapping("/dashboard/notification-toggle")
    public String toggleNotification(@AuthenticationPrincipal Principal principal) {
        dashboardService.toggleNotification(principal.getUser());
        return "redirect:/dashboard";
    }

    // todo 페이지
    @GetMapping("/todo")
    public String showTodo() {
        return "todo";
    }

}
