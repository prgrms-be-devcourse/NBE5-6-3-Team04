package com.grepp.nbe562team04.model.goalcompany;

import com.grepp.nbe562team04.model.achievement.AchievementService;
import com.grepp.nbe562team04.model.goal.GoalRepository;
import com.grepp.nbe562team04.model.goal.entity.Goal;
import com.grepp.nbe562team04.model.goalcompany.dto.GoalCompanyRequestDto;
import com.grepp.nbe562team04.model.goalcompany.dto.GoalCompanyResponseDto;
import com.grepp.nbe562team04.model.goalcompany.entity.GoalCompany;
import com.grepp.nbe562team04.model.todo.TodoRepository;
import com.grepp.nbe562team04.model.user.entity.User;
import com.grepp.nbe562team04.model.user.UserRepository;
import java.time.temporal.ChronoUnit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoalCompanyService {

    private final GoalCompanyRepository goalCompanyRepository;
    private final UserRepository userRepository;
    private final GoalRepository goalRepository;
    private final TodoRepository todoRepository;
    private final AchievementService achievementService;

    public String  createGoalCompany(GoalCompanyRequestDto dto, Long userId ) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        GoalCompany company = GoalCompany.builder()
                .user(user)
                .companyName(dto.getCompanyName())
                .content(dto.getContent())
                .status(dto.getStatus())
                .endDate(dto.getEndDate())
                .createdAt(LocalDate.now())
                .build();

        goalCompanyRepository.save(company);
        String achievementName = achievementService.giveGoalCompanyAchievement(userId);
        if (achievementName != null) return achievementName;

        achievementName = achievementService.giveThreeGoalCompaniesAchievement(userId);
        return achievementName;
    }


    // 목표 기업 단건 조회
    public GoalCompanyResponseDto getCompanyById(Long companyId) {
        GoalCompany company = goalCompanyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("해당 기업이 존재하지 않습니다."));

        GoalCompanyResponseDto dto = GoalCompanyResponseDto.builder()
                .companyName(company.getCompanyName())
                .content(company.getContent())
                .status(company.getStatus().name())
                .endDate(company.getEndDate())
                .build();

        return dto;
    }

    // 목표 기업 수정
    @Transactional
    public void updateGoalCompany(Long companyId, GoalCompanyRequestDto dto) {
        GoalCompany company = goalCompanyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("해당 기업이 존재하지 않습니다."));

        company.setCompanyName(dto.getCompanyName());
        company.setContent(dto.getContent());
        company.setStatus(dto.getStatus());
        company.setEndDate(dto.getEndDate());
    }

    // 목표 기업 삭제
    @Transactional
    public void deleteGoalCompany(Long companyId) {
        GoalCompany company = goalCompanyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("해당 기업이 존재하지 않습니다."));

        // goal 에 값이 들어 있으면 goalCompany가 삭제되지 않아 goal을 먼저 삭제하고 goalCompany를 삭제하는 로직 추가
        List<Goal> goals = goalRepository.findByCompanyCompanyId(companyId);
        for (Goal goal : goals) {
            todoRepository.deleteByGoalGoalId(goal.getGoalId()); //  각 goal에 연결된 todo 삭제
            goalRepository.delete(goal); // goal 삭제
        }
        goalCompanyRepository.delete(company);
    }

    // 알림 생성 (D-3 남았을때 트리거 발생)
    public List<GoalCompany> getUrgentGoals(User user) {
        LocalDate today = LocalDate.now();

        List<GoalCompany> companies = goalCompanyRepository.findAllByUser(user);

        return companies.stream()
            .filter(gc -> {
                if (gc.getEndDate() == null) return false;
                long dDay = ChronoUnit.DAYS.between(today, gc.getEndDate());
                return dDay >= 0 && dDay <= 3;
            })
            .toList();
    }
}
