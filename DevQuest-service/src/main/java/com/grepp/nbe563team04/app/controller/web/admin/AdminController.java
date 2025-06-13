package com.grepp.nbe563team04.app.controller.web.admin;

import com.grepp.nbe563team04.app.controller.web.member.payload.SignupRequest;
import com.grepp.nbe563team04.model.auth.code.Role;
import com.grepp.nbe563team04.model.auth.domain.Principal;
import com.grepp.nbe563team04.model.member.MemberInterestService;
import com.grepp.nbe563team04.model.member.MemberService;
import com.grepp.nbe563team04.model.member.dto.MemberDto;
import com.grepp.nbe563team04.model.member.entity.Member;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("admin")
public class AdminController {

    private final MemberInterestService memberInterestService;
    private final MemberService memberService;

    @GetMapping("signup")
    public String signup(Model model) {
        model.addAttribute("signupForm", new SignupRequest());
        return "admin/signup";
    }

    @PostMapping("signup")
    public String signup(@Valid SignupRequest form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/signup";
        }

        memberService.signup(form.toDto(), Role.ROLE_ADMIN);
        return "redirect:/member/signin";
    }

    // 관리자페이지 대시보드
    @GetMapping("dashboard")
    public String dashboard(
        @AuthenticationPrincipal Principal principal,
        Model model,
        CsrfToken csrfToken) {

        // 로그인한 관리자 정보
        Member admin = memberService.findByEmail(principal.getUsername());

        // 사용자 그룹 정보 (활성, 삭제됨, 관리자)
        Map<String, List<MemberDto>> memberGroups = memberService.findMembersGroupedByStatus();
        List<MemberDto> activeMembers = memberGroups.get("activeMembers");
        List<MemberDto> deletedMembers = memberGroups.get("deletedMembers");
        List<MemberDto> adminMembers = memberGroups.get("adminMembers");

        // 모델에 값 설정
        model.addAttribute("admin", admin);
        model.addAttribute("activeMembers", activeMembers);
        model.addAttribute("deletedMembers", deletedMembers);
        model.addAttribute("adminMembers", adminMembers);
        model.addAttribute("_csrf", csrfToken);
        model.addAttribute("nickname", principal.getUser().getNickname());
        // 대시보드 가입자/탈퇴자 추이 표 mock data
        model.addAttribute("labels", List.of("06-10", "06-11", "06-12", "06-13"));
        model.addAttribute("joinCounts", List.of(3, 5, 2, 4));
        model.addAttribute("leaveCounts", List.of(1, 0, 2, 1));

        log.info("닉네임: {}", principal.getUser().getNickname());

        return "admin/dashboard";
    }

    // 회원 관리
    @GetMapping("member-management")
    public String memberManagement(
        @AuthenticationPrincipal Principal principal,
        Model model,
        CsrfToken csrfToken) {

        // 로그인한 관리자 정보
        Member admin = memberService.findByEmail(principal.getUsername());

        // 사용자 그룹 정보 (활성, 삭제됨, 관리자)
        Map<String, List<MemberDto>> memberGroups = memberService.findMembersGroupedByStatus();
        List<MemberDto> activeMembers = memberGroups.get("activeMembers");
        List<MemberDto> deletedMembers = memberGroups.get("deletedMembers");
        List<MemberDto> adminMembers = memberGroups.get("adminMembers");

        log.info("✅ 관리자 수: {}", adminMembers != null ? adminMembers.size() : 0);
        log.info("✅ 현재 회원 수: {}", activeMembers != null ? activeMembers.size() : 0);
        log.info("✅ 탈퇴 회원 수: {}", deletedMembers != null ? deletedMembers.size() : 0);

        // 모델에 값 설정
        model.addAttribute("admin", admin);
        model.addAttribute("activeMembers", activeMembers);
        model.addAttribute("deletedMembers", deletedMembers);
        model.addAttribute("adminMembers", adminMembers);
        model.addAttribute("_csrf", csrfToken);
        model.addAttribute("nickname", principal.getUser().getNickname());

        log.info("닉네임: {}", principal.getUser().getNickname());

        return "admin/member-management";
    }

    @PostMapping("removeMember")
    public String removeMember(@RequestParam String email, RedirectAttributes redirectAttributes) {
        try {
            memberService.softDeleteUser(email);
            redirectAttributes.addFlashAttribute("message", "회원이 삭제되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "회원 삭제 중 오류가 발생했습니다.");
        }

        return "redirect:/admin/dashboard";
    }

    // 대시보드-방사형 차트
    @GetMapping("/dashboard/rader")
    public String raderChart(Model model, Principal principal) {
        String memberId = principal.getUsername();
        List<String> langLabels = memberInterestService.getTop6Langs(memberId);
        model.addAttribute("langLabels", langLabels);
        return "dashboard";
    }

}
