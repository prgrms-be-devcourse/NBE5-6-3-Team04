package com.grepp.nbe563team04.model.goalCategory.dto;

import com.grepp.nbe563team04.model.goalcompany.code.GoalStatus;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class GoalCategoryRequestDto {
    private String categoryName;
    private String color;
}
