package com.grepp.nbe563team04.app.controller.web.dashboard;

import com.grepp.nbe563team04.model.auth.domain.Principal;
import com.grepp.nbe563team04.model.dashboard.DashboardService;
import com.grepp.nbe563team04.model.dashboard.dto.DashboardDto;
import com.grepp.nbe563team04.model.member.MemberImageRepository;
import com.grepp.nbe563team04.model.member.MemberRepository;
import com.grepp.nbe563team04.model.member.entity.Member;
import com.grepp.nbe563team04.model.member.entity.MemberImage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class DashboardController {

    private final DashboardService dashboardService;
    private final MemberRepository memberRepository;
    private final MemberImageRepository memberImageRepository;

    public DashboardController(DashboardService dashboardService, MemberRepository memberRepository, MemberImageRepository memberImageRepository) {
        this.dashboardService = dashboardService;
        this.memberRepository = memberRepository;
        this.memberImageRepository = memberImageRepository;
    }

    // 대시보드
    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal Principal principal, Model model) {
        Member member = memberRepository.findById(principal.getMember().getUserId())
            .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        MemberImage image = memberImageRepository.findTopByMemberAndActivatedOrderByCreatedAtDesc(member, true)
                .orElse(null);

        DashboardDto dto = dashboardService.getDashboard(member);

        model.addAttribute("dashboard", dto);
        model.addAttribute("member", member);
        model.addAttribute("image", image);
        return "dashboard/dashboard";

    }

    // 알림 토글
    @PostMapping("/dashboard/notification-toggle")
    public String toggleNotification(@AuthenticationPrincipal Principal principal) {
        dashboardService.toggleNotification(principal.getMember());
        return "redirect:/dashboard";
    }
}
