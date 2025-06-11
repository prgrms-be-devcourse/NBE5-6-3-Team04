package com.grepp.nbe562team04.model.goalcompany.dto;

import com.grepp.nbe562team04.model.goalcompany.code.GoalStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoalCompanyResponseDto {
// 서버에서 클라이언트로 보내는 응답을 담는 dto
    private String companyName;     //회사 이름
    private String content;         // 설명(목표 내용)
    private String status;          // ex) 서류 마감 코딩테스트 과제 인적성검사 1차 면접 2차 면접 3차 면접 (컬쳐핏)
    private LocalDate endDate;      // 마감일




}
