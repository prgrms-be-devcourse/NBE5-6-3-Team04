package com.grepp.nbe562team04.model.dashboard.dto;

import com.grepp.nbe562team04.model.goalcompany.code.GoalStatus;
import lombok.Getter;

@Getter
public class AlertDto {
    private String companyName;
    private GoalStatus status; // "1차 면접", "인적성검사" 등
    private long dDay;

    public AlertDto(String companyName, GoalStatus status, long dDay) {
        this.companyName = companyName;
        this.status = status;
        this.dDay = dDay;
    }

    public String getFormattedDDay() {
        return "D-" + dDay;
    }

}
