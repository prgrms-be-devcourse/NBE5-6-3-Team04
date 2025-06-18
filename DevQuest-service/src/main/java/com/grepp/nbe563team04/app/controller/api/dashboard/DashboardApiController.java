package com.grepp.nbe563team04.app.controller.api.dashboard;

import com.grepp.nbe563team04.app.model.auth.domain.Principal;
import com.grepp.nbe563team04.app.model.dashboard.DashboardService;
import com.grepp.nbe563team04.app.model.dashboard.dto.DashboardDto;
import com.grepp.nbe563team04.app.model.member.MemberRepository;
import com.grepp.nbe563team04.app.model.member.entity.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardApiController {

    private final DashboardService dashboardService;
    private final MemberRepository memberRepository;

    public DashboardApiController(DashboardService dashboardService,
        MemberRepository memberRepository) {
        this.dashboardService = dashboardService;
        this.memberRepository = memberRepository;
    }

    // 대시보드
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardDto> getDashboardData(
        @AuthenticationPrincipal Principal principal) {
        Member member = memberRepository.findById(principal.getMember().getUserId())
            .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        DashboardDto dto = dashboardService.getDashboard(member);
        return ResponseEntity.ok(dto);
    }
}
