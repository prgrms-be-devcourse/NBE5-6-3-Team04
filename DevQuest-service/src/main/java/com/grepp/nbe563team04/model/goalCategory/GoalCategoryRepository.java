package com.grepp.nbe563team04.model.goalCategory;

import com.grepp.nbe563team04.model.goalCategory.entity.GoalCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoalCategoryRepository extends JpaRepository<GoalCategory, Long> {
    Optional<GoalCategory> findByCategoryName(String categoryName);
}
