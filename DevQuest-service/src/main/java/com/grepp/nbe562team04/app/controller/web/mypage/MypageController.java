package com.grepp.nbe562team04.app.controller.web.mypage;

import com.grepp.nbe562team04.model.achievement.AchievementService;
import com.grepp.nbe562team04.model.achievement.dto.AchievementDto;
import com.grepp.nbe562team04.model.auth.domain.Principal;
import com.grepp.nbe562team04.model.level.LevelService;
import com.grepp.nbe562team04.model.user.UserService;
import com.grepp.nbe562team04.model.user.dto.UserDto;
import com.grepp.nbe562team04.model.user.entity.User;
import com.grepp.nbe562team04.model.user.entity.UsersAchieve;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
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
@RequestMapping("user")
@RequiredArgsConstructor
public class MypageController {

    private final UserService userService;
    private final LevelService levelService;
    private final AchievementService achievementService;

    @GetMapping("mypage")
    public String index(@AuthenticationPrincipal Principal principal,
        @RequestParam(required = false) String achievement,
        @RequestParam(required = false) String name,
        Model model,
        CsrfToken csrfToken) {
        String email = principal.getUsername();
        User user = userService.findByEmail(email);
        List<UsersAchieve> usersAchieves = userService.findAchieveByUserId(user.getUserId());

        int progress = levelService.levelProgress(user);
        model.addAttribute("user", user);
        model.addAttribute("progressPercent", progress);
        model.addAttribute("userAchieve", usersAchieves);
        model.addAttribute("_csrf", csrfToken);
        return "mypage/mypage";
    }

    @GetMapping("update")
    public String showUpdatePage(@AuthenticationPrincipal Principal principal, Model model,CsrfToken csrfToken) {
        String email = principal.getUsername();
        User user = userService.findByEmail(email);

        int progress = levelService.levelProgress(user);
        model.addAttribute("user", user);
        model.addAttribute("progressPercent", progress);
        model.addAttribute("_csrf", csrfToken);
        return "mypage/mypageUpdate";
    }

    @PostMapping("update")
    public String updateUser(@AuthenticationPrincipal Principal principal,
                             @ModelAttribute UserDto dto,
                             @RequestParam("userImageFile") MultipartFile file,
                             @RequestParam("currentPassword") String currentPassword,
                             Model model,
                             RedirectAttributes redirectAttributes) throws IOException {

        String email = principal.getUsername();
        User user = userService.findByEmail(email);

        // 현재 비밀번호 확인
        if (!userService.checkPassword(user, currentPassword)) {
            int progress = levelService.levelProgress(user);
            model.addAttribute("user", user);
            model.addAttribute("passwordError", "비밀번호를 확인하세요");
            model.addAttribute("progressPercent", progress);
            return "mypage/mypageUpdate"; // 다시 수정 페이지로
        }

        userService.updateUser(email, dto, file);

        User updatedUser = userService.findByEmail(email);
        boolean isProfileComplete = updatedUser.getNickname() != null && !updatedUser.getNickname().isBlank()
                && updatedUser.getComment() != null && !updatedUser.getComment().isBlank()
                && updatedUser.getUserImage() != null && !updatedUser.getUserImage().isBlank();

        if (isProfileComplete) {
            String achievedName = achievementService.giveTutorialAchievement(updatedUser.getUserId());

            if (achievedName != null) {
                redirectAttributes.addAttribute("achievementName", URLEncoder.encode(achievedName, StandardCharsets.UTF_8));
            }
        }

        return "redirect:/user/mypage";
    }

    @GetMapping("/withdraw-success")
    public String showWithdrawSuccess() {
        return "mypage/withdrawSuccess"; // → withdrawSuccess.html
    }

    @PostMapping("delete")
    @ResponseBody
    public ResponseEntity<String> deleteAccount(@AuthenticationPrincipal Principal principal) {
        String email = principal.getUsername();
        userService.softDeleteUser(email);
        return ResponseEntity.ok("탈퇴 완료");
    }


    @GetMapping("achievements")
    @ResponseBody
    public List<AchievementDto> getUserAchievements(@AuthenticationPrincipal Principal principal) {
        User user = userService.findByEmail(principal.getUsername());
        return achievementService.getUserAchievements(user.getUserId());
    }
}
