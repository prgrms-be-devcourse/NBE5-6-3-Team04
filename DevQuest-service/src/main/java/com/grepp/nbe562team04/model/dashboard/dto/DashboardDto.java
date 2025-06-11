package com.grepp.nbe562team04.model.dashboard.dto;

import com.grepp.nbe562team04.model.interest.dto.InterestDto;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashboardDto {

    // 사용자 정보
    private String nickname;
    private String comment;
    private LocalDate createdAt;
    private long dayCount;
    private String userImage;

    // 관심 분야
    private List<InterestDto> roles;
    private List<InterestDto> devLangs;
    private List<InterestDto> frameworks;
    private List<InterestDto> interests;

    // 레벨 정보
    private String levelName;
    private int levelValue;
    private int exp;
    private int progressPercent;

    @Getter
    private String jobType;

    // 목표 기업
    private List<GoalCompanyDto> goalCompanies;

    // 알림 토글
    private boolean notificationOn;

    // 주요 알림
    private List<AlertDto> alerts;


}
