package com.grepp.nbe562team04.app.controller.web.dashboard;

import com.grepp.nbe562team04.model.auth.domain.Principal;
import com.grepp.nbe562team04.model.dashboard.DashboardRepository;
import com.grepp.nbe562team04.model.dashboard.DashboardService;
import com.grepp.nbe562team04.model.dashboard.dto.DashboardDto;
import com.grepp.nbe562team04.model.dashboard.dto.GoalCompanyDto;
import com.grepp.nbe562team04.model.goal.GoalService;
import com.grepp.nbe562team04.model.goal.dto.GoalResponseDto;
import com.grepp.nbe562team04.model.todo.TodoService;
import com.grepp.nbe562team04.model.todo.dto.TodoResponseDto;
import com.grepp.nbe562team04.model.user.UserRepository;
import com.grepp.nbe562team04.model.user.UserService;
import com.grepp.nbe562team04.model.user.entity.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;
    private final UserRepository userRepository;
    private final DashboardRepository dashboardRepository;
    private final GoalService goalService;
    private final TodoService todoService;
    private final UserService userService;


    public DashboardController(DashboardService dashboardService, UserRepository userRepository,
                               DashboardRepository dashboardRepository,  GoalService goalService, TodoService todoService,
        UserService userService) {

        this.dashboardService = dashboardService;
        this.userRepository = userRepository;
        this.dashboardRepository = dashboardRepository;
        this.goalService = goalService;
        this.todoService = todoService;
        this.userService = userService;
    }

    // 대시보드
    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal Principal principal, Model model, CsrfToken csrfToken) {
        User user = userRepository.findById(principal.getUser().getUserId())
            .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        // 💡 user.getLevel()이 LAZY라면, 여기서 미리 호출해서 로딩해두면 좋음
        user.getLevel().getLevelName(); // 강제 초기화

        DashboardDto dto = dashboardService.getDashboard(user);

        model.addAttribute("dashboard", dto);
        model.addAttribute("user", user); // ⭐️ 유저도 모델에 포함!
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
