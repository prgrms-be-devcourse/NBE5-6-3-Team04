package com.grepp.nbe563team04.app.controller.web.admin;

import com.grepp.nbe563team04.app.controller.web.user.payload.SignupRequest;
import com.grepp.nbe563team04.model.auth.code.Role;
import com.grepp.nbe563team04.model.auth.domain.Principal;
import com.grepp.nbe563team04.model.interest.InterestService;
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
@RequestMapping("admin/company-stats")
public class AdminCompanyController {

    private final UserService userService;

    // 기업 통계
    @GetMapping
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
