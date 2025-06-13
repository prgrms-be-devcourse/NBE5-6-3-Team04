package com.grepp.nbe563team04.app.controller.web.member;

import com.grepp.nbe563team04.app.controller.web.member.payload.SigninRequest;
import com.grepp.nbe563team04.app.controller.web.member.payload.SignupRequest;
import com.grepp.nbe563team04.model.auth.code.Role;
import com.grepp.nbe563team04.model.interest.InterestService;
import com.grepp.nbe563team04.model.interest.dto.InterestDto;
import com.grepp.nbe563team04.model.member.MemberService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final InterestService interestService;

    // 로그인 폼
    @GetMapping("signin")
    public String signin(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:/dashboard";
        }

        model.addAttribute("signinRequest", new SigninRequest());
        return "member/signin";
    }


    // 회원가입 폼
    @GetMapping("signup")
    public String signup(Model model) {
        model.addAttribute("signupForm", new SignupRequest());
        return "member/signup";
    }

    // 회원가입 > 관심분야 선택
    @PostMapping("signup")
    public String signup(
        @Valid SignupRequest form,
        BindingResult bindingResult,
        HttpSession session
    ) {
        if (bindingResult.hasErrors()) {
            return "member/signup";
        }

        session.setAttribute("signupForm", form);
        return "redirect:/member/interests";
    }


    // 관심분야 전체 조회
    @GetMapping("member/interests")
    public String interestList(Model model) {
        Map<String, List<InterestDto>> interestGroup = interestService.findInterestGroupByType();

        model.addAttribute("interestRoles", interestGroup.get("interestRoles"));
        model.addAttribute("interestSkills", interestGroup.get("interestSkills"));

        return "member/interests";
    }

    // 관심분야 등록
    @PostMapping("member/interests")
    public String receiveInterests(
        HttpSession session,
        @RequestParam("role") Long roleId,
        @RequestParam("skills") String skills
    ) {
        SignupRequest signupForm = (SignupRequest) session.getAttribute("signupForm");
        if (signupForm == null) {

            return "redirect:/member/signup";
        }

        List<Long> skillIds = Arrays.stream(skills.split(","))
            .map(Long::parseLong)
            .collect(Collectors.toList());

        log.info("여기까지 왔다");

        Long userId = memberService.signup(signupForm.toDto(), Role.ROLE_USER);
        memberService.receiveInterest(userId, roleId, skillIds);

        session.removeAttribute("signupForm");
        return "member/signupSuccess";
    }

}
