package com.grepp.nbe563team04.model.goalcompany;

import com.grepp.nbe563team04.model.achievement.AchievementService;
import com.grepp.nbe563team04.model.company.CompanyNormalizationService;
import com.grepp.nbe563team04.model.company.NormalizedCompanyRepository;
import com.grepp.nbe563team04.model.company.entity.NormalizedCompany;
import com.grepp.nbe563team04.model.goal.GoalRepository;
import com.grepp.nbe563team04.model.goal.entity.Goal;
import com.grepp.nbe563team04.model.goalcompany.dto.GoalCompanyRequestDto;
import com.grepp.nbe563team04.model.goalcompany.dto.GoalCompanyResponseDto;
import com.grepp.nbe563team04.model.goalcompany.entity.GoalCompany;
import com.grepp.nbe563team04.model.member.entity.Member;
import com.grepp.nbe563team04.model.todo.TodoRepository;
import com.grepp.nbe563team04.model.member.MemberRepository;
import java.time.temporal.ChronoUnit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoalCompanyService {

    private final MemberRepository memberRepository;

    private final GoalCompanyRepository goalCompanyRepository;
    private final GoalRepository goalRepository;
    private final TodoRepository todoRepository;

    private final AchievementService achievementService;
    private final CompanyNormalizationService companyNormalizationService;
    private final NormalizedCompanyRepository normalizedCompanyRepository;


    // 목표 기업 생성
    @Transactional
    public String createGoalCompany(GoalCompanyRequestDto dto, Long userId) {
        Member member = memberRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Member not found"));

        // 기업명 정규화
        String normalizedCompanyName = companyNormalizationService.normalizeCompanyName(dto.getCompanyName());
        NormalizedCompany normalizedCompany = normalizedCompanyRepository
            .findByStandardName(normalizedCompanyName)
            .orElseThrow(() -> new RuntimeException("정규화된 기업명을 찾을 수 없습니다."));

        // GoalCompany Entity 생성
        GoalCompany company = GoalCompany.builder()
            .member(member)
            .companyName(normalizedCompanyName)  // 정규화된 기업명 사용
            .content(dto.getContent())
            .status(dto.getStatus())
            .endDate(dto.getEndDate())
            .createdAt(LocalDate.now())
            .normalizedCompany(normalizedCompany)
            .build();

        goalCompanyRepository.save(company);

        String achievementName = achievementService.giveGoalCompanyAchievement(userId);
        if (achievementName != null) return achievementName;

        achievementName = achievementService.giveThreeGoalCompaniesAchievement(userId);
        return achievementName;
    }

    // 목표 기업 단건 조회
    public GoalCompanyResponseDto getGoalCompanyById(Long companyId) {
        GoalCompany company = goalCompanyRepository.findById(companyId) // 회사 id 로 회사 불러오기
            .orElseThrow(() -> new RuntimeException("해당 기업이 존재하지 않습니다."));

        // GoalCompanyResponseDto dto 생성 - 서버에서 클라이언트로 보내기 위한 Dto 생성 ( Entity -> Dto 변환)
        GoalCompanyResponseDto dto = GoalCompanyResponseDto.builder()
            .companyId(companyId)
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

        // 기업명 정규화
        String normalizedCompanyName = companyNormalizationService.normalizeCompanyName(dto.getCompanyName());

        company.setCompanyName(normalizedCompanyName);  // 정규화된 기업명 사용
        company.setContent(dto.getContent());
        company.setStatus(dto.getStatus());
        company.setEndDate(dto.getEndDate());
    }

    // 목표 기업 삭제
    @Transactional
    public void deleteGoalCompany(Long companyId) {
        GoalCompany company = goalCompanyRepository.findById(companyId) // 회사 id 로 회사 불러오기
            .orElseThrow(() -> new RuntimeException("해당 기업이 존재하지 않습니다."));

        // goal 에 값이 들어 있으면 goalCompany가 삭제되지 않아 goal을 먼저 삭제하고 goalCompany를 삭제하는 로직 추가
        List<Goal> goals = goalRepository.findByCompanyCompanyId(companyId);
        for (Goal goal : goals) {
            todoRepository.deleteByGoalGoalId(goal.getGoalId()); //  각 goal에 연결된 투두 삭제
            goalRepository.delete(goal); // goal 삭제
        }
        goalCompanyRepository.delete(company); // goalCompany 삭제
    }

    // 알림 생성 (D-3 남았을때 트리거 발생)
    public List<GoalCompany> getUrgentGoals(Member member) {
        LocalDate today = LocalDate.now();

        List<GoalCompany> companies = goalCompanyRepository.findAllByMember(member);

        return companies.stream()
            .filter(gc -> {
                if (gc.getEndDate() == null) return false;
                long dDay = ChronoUnit.DAYS.between(today, gc.getEndDate());
                return dDay >= 0 && dDay <= 3;
            })
            .toList();
    }

    // 기존 DB에 저장된 GoalCompany의 companyName을 정규화된 값으로 일괄 업데이트하는 메서드
    @Transactional
    public void normalizeAllGoalCompanyNames() {
        // 모든 GoalCompany 목록을 조회
        List<GoalCompany> companies = goalCompanyRepository.findAll();
        for (GoalCompany company : companies) {
            // 원본 기업명을 정규화 서비스를 통해 정규화
            String original = company.getCompanyName();
            String normalized = companyNormalizationService.normalizeCompanyName(original);
            // 정규화된 기업명으로 업데이트 (JPA dirty checking으로 자동 반영)
            normalizedCompanyRepository.findByStandardName(normalized)
                .ifPresent(company::setNormalizedCompany);
        }
    }
}
