package com.grepp.nbe562team04.model.dashboard;

import com.grepp.nbe562team04.model.dashboard.dto.AlertDto;
import com.grepp.nbe562team04.model.dashboard.dto.DashboardDto;
import com.grepp.nbe562team04.model.dashboard.dto.GoalCompanyDto;
import com.grepp.nbe562team04.model.goalcompany.GoalCompanyRepository;
import com.grepp.nbe562team04.model.goalcompany.code.GoalStatus;
import com.grepp.nbe562team04.model.goalcompany.entity.GoalCompany;
import com.grepp.nbe562team04.model.interest.code.Type;
import com.grepp.nbe562team04.model.interest.dto.InterestDto;
import com.grepp.nbe562team04.model.interest.entity.Interest;
import com.grepp.nbe562team04.model.level.LevelRepository;
import com.grepp.nbe562team04.model.level.entity.Level;
import com.grepp.nbe562team04.model.user.UserRepository;
import com.grepp.nbe562team04.model.user.entity.User;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private final DashboardRepository dashboardRepository;
    private final UserRepository userRepository;
    private final LevelRepository levelRepository;

    public DashboardService(DashboardRepository dashboardRepository,
        UserRepository userRepository, GoalCompanyRepository goalCompanyRepository,
        LevelRepository levelRepository) {
        this.dashboardRepository = dashboardRepository;
        this.userRepository = userRepository;
        this.levelRepository = levelRepository;
    }

    // 대시보드 조회
    @Transactional
    public DashboardDto getDashboard(User user) {

        user.getUserInterests().forEach(ui -> ui.getInterest().getInterestName());

        // 사용자 정보
        DashboardDto dto = new DashboardDto();
        dto.setNickname(user.getNickname());
        dto.setComment(user.getComment());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUserImage(user.getUserImage());

        long dayCount = ChronoUnit.DAYS.between(user.getCreatedAt().atStartOfDay().toLocalDate(),
            LocalDate.now()) + 1;
        dto.setDayCount(dayCount);

        // 관심 분야 필터링
        List<InterestDto> interests = user.getUserInterests().stream()
            .map(userInterest -> {
                Interest interest = userInterest.getInterest();
                return new InterestDto(interest);
            })
            .toList();
        dto.setInterests(interests);

        // type으로 필터링
        List<InterestDto> roles = interests.stream()
            .filter(i -> i.getType() == Type.ROLE)
            .toList();

        List<InterestDto> skills = interests.stream()
            .filter(i -> i.getType() == Type.SKILL)
            .toList();

        // 빈 리스트 방어처리
        dto.setRoles(
            roles.isEmpty()
                ? List.of(new InterestDto(null, Type.ROLE, "직무 없음", null))
                : roles
        );

        dto.setDevLangs(
            skills.isEmpty()
                ? List.of(new InterestDto(null, Type.SKILL, "언어 없음", null))
                : skills
        );

        // 현재 레벨 계산
        Level currentLevel = levelRepository.findTopByXpLessThanEqualOrderByXpDesc(user.getExp())
            .orElseThrow(() -> new IllegalStateException("레벨 데이터가 없습니다."));
        user.setLevel(currentLevel);

        // 다음 레벨 계산
        Optional<Level> nextLevelOpt = levelRepository.findTopByXpGreaterThanOrderByXpAsc(
            user.getExp());

        // xp bar - 진행률 계산
        int progress = 0;
        if (nextLevelOpt.isPresent()) {
            Level nextLevel = nextLevelOpt.get();
            int curExp = user.getExp();
            int curXp = currentLevel.getXp();
            int nextXp = nextLevel.getXp();

            progress = (int) (((double) (curExp - curXp) / (nextXp - curXp)) * 100);
        } else {
            progress = 100;
        }

        // 레벨 정보 - 대시보드 반영
        dto.setLevelName(user.getLevel().getLevelName());
        dto.setLevelValue(user.getLevel().getLevelId().intValue());
        dto.setExp(user.getExp());
        dto.setProgressPercent(progress);

        // 알림 토글
        dto.setNotificationOn(user.isNotificationOn());

        // 목표기업 정보
        List<GoalCompany> goalCompanies = dashboardRepository.findGoalCompaniesByUser(user);
        List<GoalCompanyDto> companyDtos = goalCompanies.stream()
            .map(this::convertToDto)
            .filter(Objects::nonNull)
            .sorted(Comparator.comparing(GoalCompanyDto::getEndDate))
            .toList();

        dto.setGoalCompanies(companyDtos);

        // 주요 알림 : D-7 이내 알림만 화면에 표시 & 정렬
        LocalDate today = LocalDate.now();
        List<AlertDto> alerts = goalCompanies.stream()
            .map(g -> {
                LocalDate endDate = g.getEndDate(); // goal이 아니라 goalCompany의 endDate 사용
                if (endDate == null) {
                    return null;
                }

                long dDay = ChronoUnit.DAYS.between(today, endDate);
                GoalStatus status = g.getStatus();

                return new AlertDto(
                    g.getCompanyName(),
                    status,
                    dDay
                );
            })
            .filter(Objects::nonNull)
            .filter(alert -> alert.getDDay() >= 0 && alert.getDDay() <= 7)
            .sorted(Comparator.comparingLong(AlertDto::getDDay))
            .toList();
        System.out.println("🔔 ALERT COUNT: " + alerts.size());
        alerts.forEach(a -> System.out.println(
            a.getCompanyName() + " / " + a.getStatus() + " / D-" + a.getDDay()));

        dto.setAlerts(alerts);
        return dto;
    }

    // Id 로 목표기업 조회
    public GoalCompanyDto getCompanyDetailById(Long id) {
        GoalCompany company = dashboardRepository.findByCompanyId(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회사가 존재하지 않습니다: " + id));
        return convertToDto(company);
    }

    // 알림 토글 처리 로직
    @Transactional
    public void toggleNotification(User user) {
        User managedUser = userRepository.findById(user.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("유저 없음"));
        managedUser.setNotificationOn(!managedUser.isNotificationOn());
    }

    private GoalCompanyDto convertToDto(GoalCompany company) {
        long dDay = ChronoUnit.DAYS.between(LocalDate.now(), company.getEndDate());

        if (dDay < 0) {
            return null;
        }

        GoalCompanyDto dto = new GoalCompanyDto();
        dto.setCompanyId(company.getCompanyId());
        dto.setCompanyName(company.getCompanyName());
        dto.setStatus(company.getStatus());
        dto.setContent(company.getContent());

        if (!company.getGoals().isEmpty()) {
            dto.setStartDate(company.getGoals().get(0).getStartDate());
        }
        dto.setStatusLabel(company.getStatus().getLabel());
        dto.setEndDate(company.getEndDate());
        dto.setDDay(dDay);

        return dto;
    }
}