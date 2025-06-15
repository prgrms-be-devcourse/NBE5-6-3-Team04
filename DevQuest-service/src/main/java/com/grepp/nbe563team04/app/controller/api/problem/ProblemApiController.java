package com.grepp.nbe563team04.app.controller.api.problem;

import com.grepp.nbe563team04.model.auth.domain.Principal;
import com.grepp.nbe563team04.model.goalcompany.GoalCompanyService;
import com.grepp.nbe563team04.model.goalcompany.dto.GoalCompanyRequestDto;
import com.grepp.nbe563team04.model.goalcompany.dto.GoalCompanyResponseDto;
import com.grepp.nbe563team04.model.problem.ProblemService;
import com.grepp.nbe563team04.model.problem.dto.ProblemRequestDto;
import com.grepp.nbe563team04.model.problem.dto.ProblemResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/problem")
public class ProblemApiController {

    // 생성자 주입
    private final ProblemService problemService;


    // 목표 기업 생성
    @GetMapping("/select") // /companies/create
    public ResponseEntity<?> getAllProblems( @AuthenticationPrincipal Principal principal) { // json -> dto 자동 변환
        Long userId = principal.getUser().getUserId();

        List<ProblemResponseDto> problems = problemService.getAllProblems();

        return ResponseEntity.ok(problems);
}}