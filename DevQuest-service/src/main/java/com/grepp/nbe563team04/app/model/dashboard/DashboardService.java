package com.grepp.nbe563team04.app.model.dashboard;

import com.grepp.nbe563team04.app.model.dashboard.dto.AlertDto;
import com.grepp.nbe563team04.app.model.dashboard.dto.DashboardDto;
import com.grepp.nbe563team04.app.model.dashboard.dto.GoalCompanyDto;
import com.grepp.nbe563team04.app.model.goalcompany.GoalCompanyRepository;
import com.grepp.nbe563team04.app.model.goalcompany.code.GoalStatus;
import com.grepp.nbe563team04.app.model.goalcompany.entity.GoalCompany;
import com.grepp.nbe563team04.app.model.interest.code.Type;
import com.grepp.nbe563team04.app.model.interest.dto.InterestDto;
import com.grepp.nbe563team04.app.model.interest.entity.Interest;
import com.grepp.nbe563team04.app.model.level.LevelRepository;
import com.grepp.nbe563team04.app.model.level.entity.Level;
import com.grepp.nbe563team04.app.model.member.MemberRepository;
import com.grepp.nbe563team04.app.model.member.entity.Member;
import java.awt.Color;
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
    private final MemberRepository memberRepository;
    private final LevelRepository levelRepository;

    public DashboardService(DashboardRepository dashboardRepository,
        MemberRepository memberRepository, GoalCompanyRepository goalCompanyRepository,
        LevelRepository levelRepository) {
        this.dashboardRepository = dashboardRepository;
        this.memberRepository = memberRepository;
        this.levelRepository = levelRepository;
    }

    // 대시보드 조회
    @Transactional
    public DashboardDto getDashboard(Member member) {

        member.getMemberInterests().forEach(ui -> ui.getInterest().getInterestName());

        // 사용자 정보
        DashboardDto dto = new DashboardDto();
        dto.setNickname(member.getNickname());
        dto.setComment(member.getComment());
        dto.setCreatedAt(member.getCreatedAt());
//        dto.setUserImage(member.getUserImage());

        long dayCount = ChronoUnit.DAYS.between(member.getCreatedAt().atStartOfDay().toLocalDate(),
            LocalDate.now()) + 1;
        dto.setDayCount(dayCount);

        // 관심 분야 필터링
        List<InterestDto> interests = member.getMemberInterests().stream()
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
        Level currentLevel = levelRepository.findTopByXpLessThanEqualOrderByXpDesc(member.getExp())
            .orElseThrow(() -> new IllegalStateException("레벨 데이터가 없습니다."));
        member.setLevel(currentLevel);

        // 다음 레벨 계산
        Optional<Level> nextLevelOpt = levelRepository.findTopByXpGreaterThanOrderByXpAsc(
            member.getExp());

        // xp bar - 진행률 계산
        int progress = 0;
        if (nextLevelOpt.isPresent()) {
            Level nextLevel = nextLevelOpt.get();
            int curExp = member.getExp();
            int curXp = currentLevel.getXp();
            int nextXp = nextLevel.getXp();

            progress = (int) (((double) (curExp - curXp) / (nextXp - curXp)) * 100);
        } else {
            progress = 100;
        }

        // 레벨 정보 - 대시보드 반영
        dto.setLevelName(member.getLevel().getLevelName());
        dto.setLevelValue(member.getLevel().getLevelId().intValue());
        dto.setExp(member.getExp());
        dto.setProgressPercent(progress);

        // 알림 토글
        dto.setNotificationOn(member.isNotificationOn());

        // 목표기업 정보
        List<GoalCompany> goalCompanies = dashboardRepository.findGoalCompaniesByMember(member);
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
    public void toggleNotification(Member member) {
        Member managedMember = memberRepository.findById(member.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("유저 없음"));
        managedMember.setNotificationOn(!managedMember.isNotificationOn());
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

        String companyName = company.getCompanyName().toLowerCase();

        String style = switch (companyName) {
            case "네이버" ->
                "background-color: rgba(232, 245, 233, 0.5); color: #1a7f37; border: 2px solid #81c784;";
            case "토스" ->
                "background-color: rgba(232, 241, 255, 0.5); color: #0075ff; border: 2px solid #64b5f6;";
            case "당근" ->
                "background-color: rgba(255, 243, 224, 0.5); color: #f57c00; border: 2px solid #f57c00;";
            case "배달의민족" ->
                "background-color: rgba(224, 247, 250, 0.5); color: #00c4c4; border: 2px solid #00c4c4;";
            case "카카오" ->
                "background-color: rgba(255, 248, 225, 0.5); color: #3c1e1e; border: 2px solid #f2c300;";
            default -> "background-color: #f5f5f5; color: #333; border: 2px solid #ccc;";
        };

        dto.setStyle(style);

        // 1. 관리자에서 지정한 색상
        String textColor = (company.getNormalizedCompany() != null
            && company.getNormalizedCompany().getColor() != null)
            ? company.getNormalizedCompany().getColor()
            : "#000000"; // 기본값은 검정

        dto.setTextColor(textColor);

        // 2. 연한 배경색 만들기 (투명도 0.1로)
        String transparentBackground = textColorToRgba(textColor, 0.1);
        dto.setColor(transparentBackground);  // 배경에 쓰기

        // 3. 테두리도 살짝 연하게
        String borderColor = textColorToRgba(textColor, 0.4);
        dto.setBorderColor(borderColor);
        return dto;
    }

    // HEX 색상을 RGBA로 변환
    private String textColorToRgba(String hexColor, double alpha) {
        try {
            Color color = Color.decode(hexColor);
            return String.format("rgba(%d, %d, %d, %.2f)",
                color.getRed(), color.getGreen(), color.getBlue(), alpha);
        } catch (NumberFormatException e) {
            return "rgba(0, 0, 0, 0.1)"; // fallback
        }
    }
}