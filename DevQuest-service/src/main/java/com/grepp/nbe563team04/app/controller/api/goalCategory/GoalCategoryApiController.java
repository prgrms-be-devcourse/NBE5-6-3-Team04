package com.grepp.nbe563team04.app.controller.api.goalCategory;

import com.grepp.nbe563team04.app.model.goalCategory.GoalCategoryRepository;
import com.grepp.nbe563team04.app.model.goalCategory.entity.GoalCategory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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