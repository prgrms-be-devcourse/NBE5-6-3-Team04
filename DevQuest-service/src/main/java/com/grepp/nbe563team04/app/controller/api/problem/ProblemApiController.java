package com.grepp.nbe563team04.app.controller.api.problem;

import com.grepp.nbe563team04.model.auth.domain.Principal;
import com.grepp.nbe563team04.model.problem.ProblemService;
import com.grepp.nbe563team04.model.problem.dto.ProblemResponseDto;
import com.grepp.nbe563team04.model.userProblemSolve.UserProblemSolveService;
import com.grepp.nbe563team04.model.userProblemSolve.dto.UserProblemSolveRequestDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/problem")
public class ProblemApiController {

    // 생성자 주입
    private final ProblemService problemService;
    private final UserProblemSolveService userProblemSolveService;

    // 추천문제 불러오기
    @GetMapping("/select")
    public ResponseEntity<?> getAllProblems( @AuthenticationPrincipal Principal principal) { // json -> dto 자동 변환
        Long userId = principal.getMember().getUserId();

        List<ProblemResponseDto> problems = problemService.getAllProblemsWithSolveCount(userId);

        return ResponseEntity.ok(problems);
    }

    // 추천 문제 선택 후 저장 하기
    @PostMapping("/save")
    public ResponseEntity<?> saveUserSolvedProblems(@RequestBody UserProblemSolveRequestDto dto, @AuthenticationPrincipal Principal principal) {
        Long userId = principal.getMember().getUserId();

        userProblemSolveService.saveAll(userId, dto.getProblemIds());
        return ResponseEntity.ok("저장 완료");
    }




}