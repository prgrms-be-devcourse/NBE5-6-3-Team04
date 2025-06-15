package com.grepp.nbe563team04.model.problem.dto;

import com.grepp.nbe563team04.model.goalcompany.code.GoalStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProblemRequestDto {
// 클라이언트가 서버에 보내는 요청을 담는 dto
    private long problemId;             // 생성 시엔 생략 가능


}
