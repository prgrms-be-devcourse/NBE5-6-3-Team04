package com.grepp.nbe563team04.app.controller.api.goalCompany;

import com.grepp.nbe563team04.model.auth.domain.Principal;
import com.grepp.nbe563team04.model.goalcompany.GoalCompanyService;
import com.grepp.nbe563team04.model.goalcompany.dto.GoalCompanyRequestDto;
import com.grepp.nbe563team04.model.goalcompany.dto.GoalCompanyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/companies")
public class GoalCompanyApiController {

    // 생성자 주입
    private final GoalCompanyService goalCompanyService;

    // 목표 기업 생성
    @PostMapping("/create") // /companies/create
    public ResponseEntity<?> createCompany(@RequestBody GoalCompanyRequestDto dto, @AuthenticationPrincipal Principal principal) { // json -> dto 자동 변환
        Long userId = principal.getMember().getUserId();
        String achievementName = goalCompanyService.createGoalCompany(dto, userId);// 로그인된 유저 ID 꺼내기

        if (achievementName != null) {
            return ResponseEntity.ok(Map.of(
                    "message", "등록 완료",
                    "achievementName", achievementName
            ));
        } else {
            return ResponseEntity.ok(Map.of(
                    "message", "등록 완료"
            ));
        }
    }

    // 목표 기업 단건 조회(단순 dto 조회용)
    @GetMapping("/{companyId}") // /companies/{companyId}
    public GoalCompanyResponseDto selectCompany(@PathVariable Long companyId) {
        return goalCompanyService.getGoalCompanyById(companyId);
    }

    // 목표 기업 수정
    @PutMapping("/{companyId}/update") // /companies/{companyId}/update
    public ResponseEntity<String> updateCompany(@PathVariable Long companyId, @RequestBody GoalCompanyRequestDto dto) {
        goalCompanyService.updateGoalCompany(companyId, dto);
        return ResponseEntity.ok("수정 완료");
    }

    // 목표 기업 삭제
    @DeleteMapping("/{companyId}/delete") // /companies/{companyId}/delete
    public ResponseEntity<String> deleteCompany(@PathVariable Long companyId) {
        goalCompanyService.deleteGoalCompany(companyId);
        return ResponseEntity.ok("삭제 완료");
    }

}