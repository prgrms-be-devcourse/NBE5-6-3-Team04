package com.grepp.nbe563team04.app.model.member;

import com.grepp.nbe563team04.app.model.member.entity.Member;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberInterestService {

    private final MemberInterestRepository memberInterestRepository;
    private final MemberService memberService;

    public List<String> getTop5Langs(String email) {
        Member member = memberService.findByEmail(email);
        List<String> langs = memberInterestRepository.findTop5SkillsByUserId(member.getUserId());
        return langs;
    }

    /**
     * 모든 활성 회원의 언어 관심도를 한 번에 조회 (N+1 문제 해결)
     * @return 모든 회원의 언어 관심도 목록
     */
    public List<String> getAllMemberTopLangs() {
        // 모든 활성 회원의 언어 관심도를 한 번에 조회
        List<String> allLangs = memberInterestRepository.findAllActiveMemberTopLangs();
        return allLangs;
    }


    public List<Integer> getTop5LangScores(String email) {
        Member member = memberService.findByEmail(email);
        List<String> top5Langs = memberInterestRepository.findTop5SkillsByUserId(member.getUserId());

        long totalUsers = memberService.countActiveUsers();

        List<Integer> scores = top5Langs.stream()
            .map(lang -> {
                Integer count = memberInterestRepository.countByInterestName(lang);
                if (count == null || totalUsers == 0) return 0;
                int score = (int) ((double) count / totalUsers * 100);
                return score;
            })
            .collect(Collectors.toList());

        return scores;
    }
}
