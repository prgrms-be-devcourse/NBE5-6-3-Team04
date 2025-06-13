package com.grepp.nbe563team04.app.controller.api.goalCategory;

import com.grepp.nbe563team04.model.auth.domain.Principal;
import com.grepp.nbe563team04.model.goalCategory.GoalCategoryRepository;
import com.grepp.nbe563team04.model.goalCategory.entity.GoalCategory;
import com.grepp.nbe563team04.model.goalcompany.GoalCompanyService;
import com.grepp.nbe563team04.model.goalcompany.dto.GoalCompanyRequestDto;
import com.grepp.nbe563team04.model.goalcompany.dto.GoalCompanyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class GoalCategoryApiController {

    private final GoalCategoryRepository goalCategoryRepository;

    @GetMapping
    public ResponseEntity<List<GoalCategory>> getAllCategories() {
        return ResponseEntity.ok(goalCategoryRepository.findAll());
    }



}