package com.grepp.nbe562team04.model.dashboard.dto;

import com.grepp.nbe562team04.model.goalcompany.code.GoalStatus;
import java.time.LocalDate;
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
}
