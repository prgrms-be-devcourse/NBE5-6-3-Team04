package com.grepp.nbe563team04.app.controller.web.admin;

import com.grepp.nbe563team04.model.auth.domain.Principal;
import com.grepp.nbe563team04.model.member.MemberService;
import com.grepp.nbe563team04.model.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("admin/company-stats")
public class AdminCompanyController {

    private final MemberService memberService;

    // 기업 통계
    @GetMapping
    public String showCompanyStatsPage(
        @AuthenticationPrincipal Principal principal,
        Model model,
        CsrfToken csrfToken) {

        // 로그인한 관리자 정보
        Member admin = memberService.findByEmail(principal.getUsername());

        model.addAttribute("nickname", principal.getUser().getNickname());
        // model.addAttribute("data", data);
        return "admin/company-stats";
    }
}
