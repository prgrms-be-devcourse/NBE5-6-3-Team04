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
import com.grepp.nbe563team04.model.problem.entity.Problem;
import com.grepp.nbe563team04.model.todo.TodoRepository;
import com.grepp.nbe563team04.model.userProblemSolve.UserProblemSolveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final UserProblemSolveRepository userProblemSolveRepository;

    public List<ProblemResponseDto> getAllProblemsWithSolveCount(Long userId) {
        List<Problem> problems = problemRepository.findAll();

        // 사용자별 푼 문제와 횟수 조회
        List<Object[]> rawSolveCounts = userProblemSolveRepository.findSolvedCountsByUserId(userId);

        // 문제 ID -> 푼 횟수 매핑
        Map<Long, Integer> solvedCountMap = rawSolveCounts.stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> (Integer) row[1]
                ));

        // 문제 DTO 생성 + 푼 횟수 매핑
        return problems.stream()
                .map(p -> new ProblemResponseDto(
                        p.getId(),
                        p.getTitle(),
                        p.getLevel(),
                        p.getUrl(),
                        p.getSite(),
                        solvedCountMap.getOrDefault(p.getId(), 0)
                ))
                .toList();
    }




}
