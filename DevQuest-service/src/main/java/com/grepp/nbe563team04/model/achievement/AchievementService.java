package com.grepp.nbe563team04.model.achievement;

import com.grepp.nbe563team04.model.achievement.dto.AchievementDto;
import com.grepp.nbe563team04.model.achievement.entity.Achievement;
import com.grepp.nbe563team04.model.member.entity.Member;
import com.grepp.nbe563team04.model.todo.TodoRepository;
import com.grepp.nbe563team04.model.member.entity.MembersAchieve;
import com.grepp.nbe563team04.model.member.MemberRepository;
import com.grepp.nbe563team04.model.member.MembersAchieveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AchievementService {

    private final MembersAchieveRepository membersAchieveRepository;
    private final MemberRepository memberRepository;
    private final AchieveRepository achieveRepository;
    private final TodoRepository todoRepository;

    @Transactional
    public String giveTutorialAchievement(Long userId) {
        Long achieveId = 1L;

        boolean already = membersAchieveRepository.existsByMember_UserIdAndAchievement_AchieveId(userId, achieveId);
        if (already) {
            return null;
        }
        MembersAchieve ua = new MembersAchieve();
        ua.setMember(memberRepository.getReferenceById(userId));
        Achievement achievement = achieveRepository.getReferenceById(achieveId);
        ua.setAchievement(achievement);
        ua.setAchievedAt(LocalDateTime.now());

        membersAchieveRepository.save(ua);
        return achievement.getName();
    }

    @Transactional
    public String giveGoalCompanyAchievement(Long userId) {
        Long achieveId = 2L;

        boolean already = membersAchieveRepository.existsByMember_UserIdAndAchievement_AchieveId(userId, achieveId);
        if (already) return null;

        MembersAchieve ua = new MembersAchieve();
        ua.setMember(memberRepository.getReferenceById(userId));
        Achievement achievement = achieveRepository.findById(achieveId).orElseThrow();
        ua.setAchievement(achievement);
        ua.setAchievedAt(LocalDateTime.now());

        membersAchieveRepository.save(ua);
        log.info("업적 : {}", achievement.getName());
        return achievement.getName();
    }

    @Transactional
    public String giveThreeGoalCompaniesAchievement(Long userId) {
        Long achieveId = 5L;

        boolean already = membersAchieveRepository.existsByMember_UserIdAndAchievement_AchieveId(userId, achieveId);
        if (already) {
            return null;
        }

        Member member = memberRepository.findById(userId).orElseThrow();
        int companyCount = member.getGoalCompanies().size();

        if (companyCount < 3) {
            return null;
        }

        Achievement achievement = achieveRepository.findById(achieveId).orElseThrow();
        MembersAchieve ua = new MembersAchieve();
        ua.setMember(member);
        ua.setAchievement(achievement);
        ua.setAchievedAt(LocalDateTime.now());

        membersAchieveRepository.save(ua);
        return achievement.getName();
    }

    @Transactional
    public String giveFirstGoalCreateAchievement(Long userId) {
        Long achieveId = 4L; // 업적10의 ID

        boolean already = membersAchieveRepository.existsByMember_UserIdAndAchievement_AchieveId(userId, achieveId);
        if (already) {
            return null;
        }

        Member member = memberRepository.findById(userId).orElseThrow();

        Achievement achievement = achieveRepository.findById(achieveId).orElseThrow();
        MembersAchieve ua = new MembersAchieve();
        ua.setMember(member);
        ua.setAchievement(achievement);
        ua.setAchievedAt(LocalDateTime.now());

        membersAchieveRepository.save(ua);
        return achievement.getName();
    }

    @Transactional
    public String giveTodoFirstCheckAchievement(Long userId) {
        Long achieveId = 3L;

        boolean already = membersAchieveRepository.existsByMember_UserIdAndAchievement_AchieveId(userId, achieveId);
        if (already) return null;

        Member member = memberRepository.findById(userId).orElseThrow();
        Achievement achievement = achieveRepository.findById(achieveId).orElseThrow();
        MembersAchieve ua = new MembersAchieve();
        ua.setMember(member);
        ua.setAchievement(achievement);
        ua.setAchievedAt(LocalDateTime.now());

        membersAchieveRepository.save(ua);

        return achievement.getName();
    }

    @Transactional
    public String giveTodoFiveCheckAchievement(Long userId) {
        Long achieveId = 11L;

        // 이미 업적을 획득했는지 확인
        boolean already = membersAchieveRepository.existsByMember_UserIdAndAchievement_AchieveId(userId, achieveId);
        if (already) return null;

        Member member = memberRepository.findById(userId).orElseThrow();

        // 완료된 Todo 개수
        long doneCount = todoRepository.countByGoal_Company_Member_UserIdAndIsDoneTrue(userId);

        if (doneCount < 5) {
            return null;
        }

        Achievement achievement = achieveRepository.findById(achieveId).orElseThrow();
        MembersAchieve ua = new MembersAchieve();
        ua.setMember(member);
        ua.setAchievement(achievement);
        ua.setAchievedAt(LocalDateTime.now());

        membersAchieveRepository.save(ua);
        return achievement.getName();
    }

    public List<AchievementDto> getUserAchievements(Long userId) {
        List<MembersAchieve> membersAchievements = membersAchieveRepository.findWithAchievementByUserId(userId);
        return membersAchievements.stream()
                .map(ua -> new AchievementDto(
                        ua.getAchievement().getName(),
                        ua.getAchievement().getDescription()
                )).toList();
    }


    public List<AchievementDto> getSortedAchievementsWithStatus(Long userId) {
        List<Achievement> allAchievements = achieveRepository.findAll();
        List<Long> achievedIds = membersAchieveRepository.findAchievedIdsByUserId(userId);
        Set<Long> achievedSet = new HashSet<>(achievedIds);

        log.info("💡 전체 업적 수: {}", allAchievements.size());
        log.info("💡 사용자 획득 업적 ID: {}", achievedSet);

        return allAchievements.stream()
                .map(a -> new AchievementDto(
                        a.getName(),
                        a.getDescription(),
                        achievedSet.contains(a.getAchieveId())
                ))
                .sorted((a1, a2) -> Boolean.compare(!a1.getAchieved(), !a2.getAchieved()))
                .collect(Collectors.toList());
    }

}
