package com.grepp.nbe563team04.app.controller.web.admin;

import com.grepp.nbe563team04.app.controller.web.user.payload.SignupRequest;
import com.grepp.nbe563team04.model.auth.code.Role;
import com.grepp.nbe563team04.model.auth.domain.Principal;
import com.grepp.nbe563team04.model.user.UserService;
import com.grepp.nbe563team04.model.user.dto.UserDto;
import com.grepp.nbe563team04.model.user.entity.User;
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

    private final UserService userService;

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

        userService.signup(form.toDto(), Role.ROLE_ADMIN);
        return "redirect:/user/signin";
    }

    // 관리자페이지 대시보드
    @GetMapping("dashboard")
    public String dashboard(
        @AuthenticationPrincipal Principal principal,
        Model model,
        CsrfToken csrfToken) {

        // 로그인한 관리자 정보
        User admin = userService.findByEmail(principal.getUsername());

        // 사용자 그룹 정보 (활성, 삭제됨, 관리자)
        Map<String, List<UserDto>> userGroups = userService.findUsersGroupedByStatus();
        List<UserDto> activeUsers = userGroups.get("activeUsers");
        List<UserDto> deletedUsers = userGroups.get("deletedUsers");
        List<UserDto> adminUsers = userGroups.get("adminUsers");

        // 모델에 값 설정
        model.addAttribute("admin", admin);
        model.addAttribute("activeUsers", activeUsers);
        model.addAttribute("deletedUsers", deletedUsers);
        model.addAttribute("adminUsers", adminUsers);
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
    @GetMapping("user-management")
    public String userManagement(
        @AuthenticationPrincipal Principal principal,
        Model model,
        CsrfToken csrfToken) {

        // 로그인한 관리자 정보
        User admin = userService.findByEmail(principal.getUsername());

        // 사용자 그룹 정보 (활성, 삭제됨, 관리자)
        Map<String, List<UserDto>> userGroups = userService.findUsersGroupedByStatus();
        List<UserDto> activeUsers = userGroups.get("activeUsers");
        List<UserDto> deletedUsers = userGroups.get("deletedUsers");
        List<UserDto> adminUsers = userGroups.get("adminUsers");

        // 모델에 값 설정
        model.addAttribute("admin", admin);
        model.addAttribute("activeUsers", activeUsers);
        model.addAttribute("deletedUsers", deletedUsers);
        model.addAttribute("adminUsers", adminUsers);
        model.addAttribute("_csrf", csrfToken);
        model.addAttribute("nickname", principal.getUser().getNickname());

        log.info("닉네임: {}", principal.getUser().getNickname());

        return "admin/user-management";
    }

    @PostMapping("removeUser")
    public String removeUser(@RequestParam String email, RedirectAttributes redirectAttributes) {
        try {
            userService.softDeleteUser(email);
            redirectAttributes.addFlashAttribute("message", "회원이 삭제되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "회원 삭제 중 오류가 발생했습니다.");
        }

        return "redirect:/admin/dashboard";
    }

    // 기업 통계
    @GetMapping("company-stats")
    public String showCompanyStatsPage(
        @AuthenticationPrincipal Principal principal,
        Model model,
        CsrfToken csrfToken) {

        // 로그인한 관리자 정보
        User admin = userService.findByEmail(principal.getUsername());

        model.addAttribute("nickname", principal.getUser().getNickname());
        // model.addAttribute("data", data);
        return "admin/company-stats";
    }

}
