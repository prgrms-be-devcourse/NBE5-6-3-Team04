package com.grepp.nbe563team04.model.dashboard.dto;

import com.grepp.nbe563team04.model.goalcompany.code.GoalStatus;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoalCompanyDto {

    private String companyName;
    private GoalStatus status;
    private long dDay;
    private long companyId;
    private String content;
    private LocalDate startDate;
    private LocalDate endDate;

    private String statusLabel; // GoalStatus 라벨을 한글로 변환

    // admin-dashboard 에서 지정한 값
    private String color;

    // dashboard 에 적용될 값
    private String style;
    private String textColor;
    private String borderColor;
}
