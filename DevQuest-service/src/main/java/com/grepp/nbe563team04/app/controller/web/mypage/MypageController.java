package com.grepp.nbe563team04.app.controller.web.mypage;

import com.grepp.nbe563team04.model.achievement.AchievementService;
import com.grepp.nbe563team04.model.achievement.dto.AchievementDto;
import com.grepp.nbe563team04.model.auth.domain.Principal;
import com.grepp.nbe563team04.model.level.LevelService;
import com.grepp.nbe563team04.model.member.MemberImageRepository;
import com.grepp.nbe563team04.model.member.MemberService;
import com.grepp.nbe563team04.model.member.dto.MemberDto;
import com.grepp.nbe563team04.model.member.entity.Member;
import com.grepp.nbe563team04.model.member.entity.MemberImage;
import com.grepp.nbe563team04.model.member.entity.MembersAchieve;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@Slf4j
@RequestMapping("member")
@RequiredArgsConstructor
public class MypageController {

    private final MemberService memberService;
    private final LevelService levelService;
    private final AchievementService achievementService;
    private final MemberImageRepository memberImageRepository;

    @GetMapping("mypage")
    public String index(@AuthenticationPrincipal Principal principal,
        @RequestParam(required = false) String achievement,
        @RequestParam(required = false) String name,
        Model model) {
        String email = principal.getUsername();
        Member member = memberService.findByEmail(email);
        List<MembersAchieve> membersAchieves = memberService.findAchieveByUserId(member.getUserId());
        MemberImage image = memberImageRepository.findTopByMemberAndActivatedOrderByCreatedAtDesc(member, true)
                .orElse(null);

        int progress = levelService.levelProgress(member);
        model.addAttribute("member", member);
        model.addAttribute("image", image);
        model.addAttribute("progressPercent", progress);
        model.addAttribute("userAchieve", membersAchieves);
        return "mypage/mypage";
    }

    @GetMapping("update")
    public String showUpdatePage(@AuthenticationPrincipal Principal principal, Model model) {
        String email = principal.getUsername();
        Member member = memberService.findByEmail(email);
        MemberImage image = memberImageRepository.findTopByMemberAndActivatedOrderByCreatedAtDesc(member, true)
                .orElse(null);

        int progress = levelService.levelProgress(member);
        model.addAttribute("member", member);
        model.addAttribute("image", image);
        model.addAttribute("progressPercent", progress);
        return "mypage/mypageUpdate";
    }

    @PostMapping("update")
    public String updateUser(@AuthenticationPrincipal Principal principal,
                             @ModelAttribute MemberDto dto,
                             @RequestParam("userImageFile") List<MultipartFile> file,
                             @RequestParam("currentPassword") String currentPassword,
                             Model model,
                             RedirectAttributes redirectAttributes) throws IOException {

        String email = principal.getUsername();
        Member member = memberService.findByEmail(email);

        // 현재 비밀번호 확인
        if (!memberService.checkPassword(member, currentPassword)) {
            int progress = levelService.levelProgress(member);
            MemberImage image = memberImageRepository.findTopByMemberAndActivatedOrderByCreatedAtDesc(
                    member, true)
                    .orElse(null);
            model.addAttribute("member", member);
            model.addAttribute("image", image);
            model.addAttribute("passwordError", "비밀번호를 확인하세요");
            model.addAttribute("progressPercent", progress);
            return "mypage/mypageUpdate"; // 다시 수정 페이지로
        }

        memberService.updateUser(email, dto, file);

        Member updatedMember = memberService.findByEmail(email);
        MemberImage latestImage = memberImageRepository.findTopByMemberAndActivatedOrderByCreatedAtDesc(
                updatedMember, true)
                .orElse(null);
        boolean isProfileComplete = updatedMember.getNickname() != null && !updatedMember.getNickname().isBlank()
                && updatedMember.getComment() != null && !updatedMember.getComment().isBlank()
                && updatedMember.isProfile();

        if (isProfileComplete) {
            String achievedName = achievementService.giveTutorialAchievement(updatedMember.getUserId());
            if (achievedName != null) {
                redirectAttributes.addAttribute("achievementName", URLEncoder.encode(achievedName, StandardCharsets.UTF_8));
            }
        }

        return "redirect:/member/mypage";
    }

    @GetMapping("/withdraw-success")
    public String showWithdrawSuccess() {
        return "mypage/withdrawSuccess"; // → withdrawSuccess.html
    }

    @PostMapping("delete")
    @ResponseBody
    public ResponseEntity<String> deleteAccount(@AuthenticationPrincipal Principal principal) {
        String email = principal.getUsername();
        memberService.softDeleteUser(email);
        return ResponseEntity.ok("탈퇴 완료");
    }


    @GetMapping("achievements")
    @ResponseBody
    public List<AchievementDto> getUserAchievements(@AuthenticationPrincipal Principal principal) {
        Member member = memberService.findByEmail(principal.getUsername());
        return achievementService.getSortedAchievementsWithStatus(member.getUserId());
    }
}
