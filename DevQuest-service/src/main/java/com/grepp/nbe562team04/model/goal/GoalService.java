package com.grepp.nbe562team04.model.goal;

import com.grepp.nbe562team04.model.achievement.AchievementService;
import com.grepp.nbe562team04.model.achievement.entity.Achievement;
import com.grepp.nbe562team04.model.goal.dto.GoalRequestDto;
import com.grepp.nbe562team04.model.goal.dto.GoalResponseDto;
import com.grepp.nbe562team04.model.goal.entity.Goal;
import com.grepp.nbe562team04.model.goalcompany.entity.GoalCompany;
import com.grepp.nbe562team04.model.goalcompany.GoalCompanyRepository;
import com.grepp.nbe562team04.model.todo.TodoRepository;
import com.grepp.nbe562team04.model.todo.entity.Todo;
import com.grepp.nbe562team04.model.user.UserRepository;
import com.grepp.nbe562team04.model.user.UserService;
import com.grepp.nbe562team04.model.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoalService {

    private final UserService userService;
    private final GoalRepository goalRepository;
    private final GoalCompanyRepository goalCompanyRepository;
    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    private final AchievementService achievementService;
    // 목표 생성
    @Transactional
    public String createGoal(GoalRequestDto dto, Long userId) {
        GoalCompany company = goalCompanyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new RuntimeException("해당 기업이 존재하지 않습니다."));

        Goal goal = Goal.builder()
                .company(company)
                .title(dto.getTitle())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .isDone(false) // 명시적으로 false 표시
                .createdAt(LocalDate.now())
                .build();

        goalRepository.save(goal);
        return achievementService.giveFirstGoalCreateAchievement(userId);
    }

    // 기업별 목표 목록 조회
    @Transactional
    public List<GoalResponseDto> getGoalsByCompanyId(Long companyId) {
        return goalRepository.findByCompanyCompanyId(companyId).stream()
                .map(goal -> {
                    List<Todo> todos = Optional.ofNullable(todoRepository.findByGoalGoalId(goal.getGoalId()))
                            .orElse(Collections.emptyList());
                    long total = todos.size();
                    long done = todos.stream()
                            .filter(todo -> Boolean.TRUE.equals(todo.getIsDone()))
                            .count();
                    int progress = total == 0 ? 0 : (int) ((done * 100.0) / total);

                    return GoalResponseDto.builder()
                            .goalId(goal.getGoalId())
                            .title(goal.getTitle())
                            .startDate(goal.getStartDate())
                            .endDate(goal.getEndDate())
                            .isDone(goal.getIsDone())
                            .createdAt(goal.getCreatedAt())
                            .progress(progress)
                            .build();
                })
                .collect(Collectors.toList());
    }



    // 목표 수정
    @Transactional
    public void updateGoal(Long goalId, GoalRequestDto dto) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("해당 목표가 존재하지 않습니다."));

        goal.setTitle(dto.getTitle());
        goal.setStartDate(dto.getStartDate());
        goal.setEndDate(dto.getEndDate());
        goal.setIsDone(dto.getIsDone());
    }

    // 목표 삭제
    @Transactional
    public void deleteGoal(Long goalId) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("해당 목표가 존재하지 않습니다."));

        todoRepository.deleteByGoalGoalId(goalId); // 목표 삭제시 하위 존재하는 투두들 함께 삭제

        goalRepository.delete(goal);
    }

    // 목표 상세 조회
    public GoalResponseDto getGoalById(Long goalId) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("목표 없음"));

        List<Todo> todos = Optional.ofNullable(todoRepository.findByGoalGoalId(goalId))
                .orElse(Collections.emptyList());
        long total = todos.size();
        long done = todos.stream()
                .filter(todo -> Boolean.TRUE.equals(todo.getIsDone()))
                .count();
        int percent = total == 0 ? 0 : (int) ((done * 100.0) / total);

        return GoalResponseDto.builder()
                .goalId(goal.getGoalId())
                .title(goal.getTitle())
                .startDate(goal.getStartDate())
                .endDate(goal.getEndDate())
                .isDone(goal.getIsDone())
                .createdAt(goal.getCreatedAt())
                .progress(percent)
                .build();
    }


    //목표 완료
    @Transactional
    public Map<String, Object> completeGoal(Long goalId, User user) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("해당 목표가 존재하지 않습니다."));

        if (goal.getIsDone()) {
            throw new IllegalStateException("이미 완료된 목표입니다.");
        }

        List<Todo> todos = todoRepository.findByGoalGoalId(goalId);
        boolean allDone = todos.stream().allMatch(todo -> Boolean.TRUE.equals(todo.getIsDone()));
        if (!allDone) {
            throw new IllegalStateException("아직 완료되지 않은 할 일이 있습니다.");
        }

        goal.setIsDone(true);
        goalRepository.save(goal);

        User dbUser = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Long beforeLevelId = dbUser.getLevel().getLevelId();
        userService.updateXpAndLevel(dbUser, 10);  // 여기에 실제 DB User 넘기기
        Long afterLevelId = dbUser.getLevel().getLevelId();
        boolean leveledUp = !beforeLevelId.equals(afterLevelId);

        Map<String, Object> result = new HashMap<>();
        result.put("leveledUp", leveledUp);
        result.put("newLevelId", afterLevelId);
        result.put("newLevelName", dbUser.getLevel().getLevelName());

        return result;
    }
}
