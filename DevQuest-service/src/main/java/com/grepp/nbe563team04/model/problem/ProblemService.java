package com.grepp.nbe563team04.model.problem;

import com.grepp.nbe563team04.model.achievement.AchievementService;
import com.grepp.nbe563team04.model.goal.GoalRepository;
import com.grepp.nbe563team04.model.goal.entity.Goal;
import com.grepp.nbe563team04.model.goalcompany.GoalCompanyRepository;
import com.grepp.nbe563team04.model.goalcompany.dto.GoalCompanyRequestDto;
import com.grepp.nbe563team04.model.goalcompany.dto.GoalCompanyResponseDto;
import com.grepp.nbe563team04.model.goalcompany.entity.GoalCompany;
import com.grepp.nbe563team04.model.member.MemberRepository;
import com.grepp.nbe563team04.model.member.entity.Member;
import com.grepp.nbe563team04.model.problem.dto.ProblemResponseDto;
import com.grepp.nbe563team04.model.todo.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;

    public List<ProblemResponseDto> getAllProblems() {
        return problemRepository.findAll().stream()
                .map(p -> new ProblemResponseDto(
                        p.getId(),
                        p.getTitle(),
                        p.getLevel(),
                        p.getUrl()
                ))
                .toList();
    }




}
