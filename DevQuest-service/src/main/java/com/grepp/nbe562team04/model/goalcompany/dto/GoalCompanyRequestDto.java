package com.grepp.nbe562team04.model.goalcompany.dto;

import com.grepp.nbe562team04.model.goalcompany.code.GoalStatus;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoalCompanyRequestDto {
// 클라이언트가 서버에 보내는 요청을 담는 dto
    private long companyId;             // 생성 시엔 생략 가능
    private String companyName;         // 회사 이름
    private String content;             // 설명 (목표 내용)
    private GoalStatus status;          // ex) 서류 마감 코딩테스트 과제 인적성검사 1차 면접 2차 면접 3차 면접 (컬쳐핏)
    private LocalDate endDate;          // 마감일




}
