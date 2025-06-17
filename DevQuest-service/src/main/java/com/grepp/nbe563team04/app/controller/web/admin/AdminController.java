package com.grepp.nbe563team04.app.controller.web.admin;

import com.grepp.nbe563team04.app.controller.web.member.payload.SignupRequest;
import com.grepp.nbe563team04.model.achievement.AchieveRepository;
import com.grepp.nbe563team04.model.achievement.AchievementService;
import com.grepp.nbe563team04.model.achievement.entity.Achievement;
import com.grepp.nbe563team04.model.auth.code.Role;
import com.grepp.nbe563team04.model.auth.domain.Principal;
import com.grepp.nbe563team04.model.member.MemberInterestService;
import com.grepp.nbe563team04.model.member.MemberService;
import com.grepp.nbe563team04.model.member.dto.MemberDto;
import com.grepp.nbe563team04.model.member.entity.Member;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("admin")
public class AdminController {

    private final MemberService memberService;
    private final MemberInterestService memberInterestService;
    private final AchievementService achievementService;
    private final AchieveRepository achieveRepository;

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
    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal Principal principal, Model model) {
        log.info("대시보드 접근 - 사용자: {}", principal.getUsername());

        try {
            // 관리자 정보 조회
            Member admin = memberService.findByEmail(principal.getUsername());
            log.info("관리자 정보 조회 완료 - 닉네임: {}", admin.getNickname());
            model.addAttribute("nickname", admin.getNickname());

            // 회원 그룹별 통계
            Map<String, List<MemberDto>> memberGroups = memberService.findMembersGroupedByStatus();
            log.info("회원 그룹 통계 조회 완료 - 그룹 수: {}", memberGroups.size());

            // null 체크 및 빈 리스트로 초기화
            List<MemberDto> activeUsers = memberGroups.get("activeUsers");
            List<MemberDto> deletedUsers = memberGroups.get("deletedUsers");
            List<MemberDto> adminUsers = memberGroups.get("adminUsers");

            activeUsers = activeUsers != null ? activeUsers : new ArrayList<>();
            deletedUsers = deletedUsers != null ? deletedUsers : new ArrayList<>();
            adminUsers = adminUsers != null ? adminUsers : new ArrayList<>();

            log.info("회원 그룹 크기 - 활성: {}, 탈퇴: {}, 관리자: {}",
                activeUsers.size(), deletedUsers.size(), adminUsers.size());

            model.addAttribute("activeMembers", activeUsers);
            model.addAttribute("deletedMembers", deletedUsers);
            model.addAttribute("adminMembers", adminUsers);

            // 차트 데이터 조회 시작
            log.info("차트 데이터 조회 시작");

            // 모든 활성 회원의 언어 관심도 데이터 수집 - N+1 문제 해결
            Map<String, Integer> langCounts = new HashMap<>();
            // 한 번에 모든 회원의 언어 관심도를 조회하는 메서드 사용
            List<String> allMemberLangs = memberInterestService.getAllMemberTopLangs();
            for (String lang : allMemberLangs) {
                langCounts.merge(lang, 1, Integer::sum);
            }

            // 상위 6개 언어 선택
            List<Map.Entry<String, Integer>> topLangs = langCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(6)
                .collect(Collectors.toList());

            List<String> langLabels = topLangs.stream()
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

            List<Integer> langScores = topLangs.stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

            log.info("언어 관심도 데이터 - 언어: {}, 점수: {}", langLabels, langScores);

            model.addAttribute("langLabels", langLabels);
            model.addAttribute("langScores", langScores);

            // 회원 가입/탈퇴 데이터 (최근 7일, 일별)
            List<String> labels = new ArrayList<>();
            List<Integer> joinData = new ArrayList<>();
            List<Integer> leaveData = new ArrayList<>();
            LocalDate today = LocalDate.now();
            for (int i = 6; i >= 0; i--) {
                LocalDate date = today.minusDays(i);
                labels.add(date.toString());
                int joinCount = (int) activeUsers.stream()
                    .filter(u -> u.getCreatedAt() != null && u.getCreatedAt().equals(date))
                    .count();
                int leaveCount = (int) deletedUsers.stream()
                    .filter(u -> u.getDeletedAt() != null && u.getDeletedAt().equals(date))
                    .count();
                joinData.add(joinCount);
                leaveData.add(leaveCount);
            }
            model.addAttribute("labels", labels);
            model.addAttribute("joinData", joinData);
            model.addAttribute("leaveData", leaveData);

            // 오늘 가입/탈퇴한 사용자 수 계산 및 바인딩
            int todayJoin = (int) activeUsers.stream()
                .filter(u -> u.getCreatedAt() != null && u.getCreatedAt().equals(LocalDate.now()))
                .count();
            int todayLeave = (int) deletedUsers.stream()
                .filter(u -> u.getDeletedAt() != null && u.getDeletedAt().equals(LocalDate.now()))
                .count();
            model.addAttribute("todayJoin", todayJoin);
            model.addAttribute("todayLeave", todayLeave);

            return "admin/dashboard";
        } catch (Exception e) {
            log.error("대시보드 데이터 조회 중 오류 발생", e);
            throw e;
        }
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
        List<MemberDto> activeMembers = memberGroups.get("activeUsers");
        List<MemberDto> deletedMembers = memberGroups.get("deletedUsers");
        List<MemberDto> adminMembers = memberGroups.get("adminUsers");

        // null 체크 (안전하게)
        if (activeMembers == null) activeMembers = List.of();
        if (deletedMembers == null) deletedMembers = List.of();
        if (adminMembers == null) adminMembers = List.of();

        log.info("✅ 관리자 수: {}", adminMembers != null ? adminMembers.size() : 0);
        log.info("✅ 현재 회원 수: {}", activeMembers != null ? activeMembers.size() : 0);
        log.info("✅ 탈퇴 회원 수: {}", deletedMembers != null ? deletedMembers.size() : 0);

        // 모델에 값 설정
        model.addAttribute("admin", admin);
        model.addAttribute("activeMembers", activeMembers);
        model.addAttribute("deletedMembers", deletedMembers);
        model.addAttribute("adminMembers", adminMembers);
        model.addAttribute("_csrf", csrfToken);
        model.addAttribute("nickname", principal.getMember().getNickname());

        log.info("닉네임: {}", principal.getMember().getNickname());

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

    @GetMapping("/achievement-management")
    public String achievementManagement(Model model) {
        List<Achievement> achievements = achieveRepository.findAll();

        model.addAttribute("achievements", achievements);
        return "admin/achievement-management";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Achievement achievement = achieveRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("업적 없음: " + id));
        model.addAttribute("achievement", achievement);
        return "admin/edit-achievement";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Achievement updated) {
        Achievement achievement = achieveRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("업적 없음: " + id));

        achievement.setName(updated.getName());
        achievement.setDescription(updated.getDescription());
        achievement.setImageUrl(updated.getImageUrl());

        achieveRepository.save(achievement);
        return "redirect:/achievement-management";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        achieveRepository.deleteById(id);
        return "redirect:/achievement-management";
    }

    // 대시보드-방사형 차트
//    @GetMapping("/dashboard/rader")
//    public String raderChart(Model model, Principal principal) {
//        String memberId = principal.getUsername();
//        List<String> langLabels = memberInterestService.getTop6Langs(memberId);
//        model.addAttribute("langLabels", langLabels);
//        return "dashboard";
//    }

}
