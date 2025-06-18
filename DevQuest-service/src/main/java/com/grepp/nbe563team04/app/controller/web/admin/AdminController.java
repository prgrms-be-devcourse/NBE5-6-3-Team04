package com.grepp.nbe563team04.app.controller.web.admin;

import com.grepp.nbe563team04.app.controller.web.member.payload.SignupRequest;
import com.grepp.nbe563team04.app.model.achievement.AchieveRepository;
import com.grepp.nbe563team04.app.model.achievement.AchievementService;
import com.grepp.nbe563team04.app.model.achievement.entity.Achievement;
import com.grepp.nbe563team04.app.model.auth.code.Role;
import com.grepp.nbe563team04.app.model.auth.domain.Principal;
import com.grepp.nbe563team04.app.model.member.MemberImageRepository;
import com.grepp.nbe563team04.app.model.member.MemberInterestService;
import com.grepp.nbe563team04.app.model.member.MemberService;
import com.grepp.nbe563team04.app.model.member.dto.MemberDto;
import com.grepp.nbe563team04.app.model.member.entity.Member;
import com.grepp.nbe563team04.app.model.member.entity.MemberImage;
import jakarta.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
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
    private final MemberImageRepository memberImageRepository;

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
        return "redirect:/signin";
    }

    // 관리자페이지 대시보드
    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal Principal principal, Model model) {

        try {
            // 관리자 정보 조회
            Member admin = memberService.findByEmail(principal.getUsername());
            model.addAttribute("nickname", admin.getNickname());

            // 회원 그룹별 통계
            Map<String, List<MemberDto>> memberGroups = memberService.findMembersGroupedByStatus();

            // null 체크 및 빈 리스트로 초기화
            List<MemberDto> activeUsers = memberGroups.get("activeUsers");
            List<MemberDto> deletedUsers = memberGroups.get("deletedUsers");
            List<MemberDto> adminUsers = memberGroups.get("adminUsers");

            activeUsers = activeUsers != null ? activeUsers : new ArrayList<>();
            deletedUsers = deletedUsers != null ? deletedUsers : new ArrayList<>();
            adminUsers = adminUsers != null ? adminUsers : new ArrayList<>();

            model.addAttribute("activeMembers", activeUsers);
            model.addAttribute("deletedMembers", deletedUsers);
            model.addAttribute("adminMembers", adminUsers);

            // 모든 활성 회원의 언어 관심도 데이터 수집 - N+1 문제 해결
            Map<String, Integer> langCounts = new HashMap<>();
            // 한 번에 모든 회원의 언어 관심도를 조회하는 메서드 사용
            List<String> allMemberLangs = memberInterestService.getAllMemberTopLangs();
            for (String lang : allMemberLangs) {
                langCounts.merge(lang, 1, Integer::sum);
            }

            // 상위 5개 언어 선택
            List<Map.Entry<String, Integer>> topLangs = langCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .toList();

            List<String> langLabels = topLangs.stream()
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

            List<Integer> langScores = topLangs.stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

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

        // 모델에 값 설정
        model.addAttribute("admin", admin);
        model.addAttribute("activeMembers", activeMembers);
        model.addAttribute("deletedMembers", deletedMembers);
        model.addAttribute("adminMembers", adminMembers);
        model.addAttribute("_csrf", csrfToken);
        model.addAttribute("nickname", principal.getMember().getNickname());

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

    @PostMapping("/achievement/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Achievement updated,
                         @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        try {
            achievementService.updateAchievement(id, updated, imageFile);
        } catch (IOException e) {
            log.error("업적 이미지 업로드 실패", e);
        }
        return "redirect:/admin/achievement-management";
    }

    @GetMapping("/achievement/delete/{id}")
    public String delete(@PathVariable Long id) {
        achievementService.deleteAchievement(id);
        return "redirect:/admin/achievement-management";
    }


    // admin-dashboard Top3 Member 조회
    @GetMapping("/dashboard/top-members")
    @ResponseBody
    public List<MemberDto> getTopUsers() {
        return memberService.getTop3MembersByLevel()
            .stream()
            .map(MemberDto::from)
            .collect(Collectors.toList());
    }

    // 사진 조회
    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/dashboard/image/{userId}")
    @ResponseBody
    public ResponseEntity<Resource> getProfileImage(@PathVariable Long userId) {
        Member member = memberService.findById(userId);

        Optional<MemberImage> imageOpt =
            memberImageRepository.findTopByMemberAndActivatedTrueOrderByCreatedAtDesc(member);

        if (imageOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        MemberImage image = imageOpt.get();

        String fullPath = uploadPath + "/" + image.getRenameFileName();
        Resource file = new FileSystemResource(fullPath);

        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_JPEG)
            .body(file);
    }
}
