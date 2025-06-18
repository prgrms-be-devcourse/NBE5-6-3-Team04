package com.grepp.nbe563team04.model.goalCategory;

import com.grepp.nbe563team04.model.goalCategory.dto.GoalCategoryResponseDto;
import com.grepp.nbe563team04.model.goalCategory.entity.GoalCategory;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoalCategoryService {

    private final GoalCategoryRepository goalCategoryRepository;


    public List<GoalCategoryResponseDto> getGoalCategories(){
        List<GoalCategory> categories = goalCategoryRepository.findAll();

        List<GoalCategoryResponseDto> dtos = new ArrayList<>();

        for(GoalCategory category : categories){
            GoalCategoryResponseDto dto = GoalCategoryResponseDto.builder()
                    .categoryId(category.getCategoryId())
                    .categoryName(category.getCategoryName())
                    .color(category.getColor())
                    .koreanName(category.getKoreanName())
                    .build();

            dtos.add(dto);
        }
        return dtos;

    }
}
