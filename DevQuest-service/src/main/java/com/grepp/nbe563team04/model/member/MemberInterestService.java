package com.grepp.nbe563team04.model.member;

import com.grepp.nbe563team04.model.member.entity.Member;
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

    public List<String> getTop6Langs(String email) {
        log.info("getTop6Langs 호출됨 - email: {}", email);
        Member member = memberService.findByEmail(email);
        List<String> langs = memberInterestRepository.findTop6SkillsByUserId(member.getUserId());
        log.info("조회된 언어 목록: {}", langs);
        return langs;
    }

    /**
     * 모든 활성 회원의 언어 관심도를 한 번에 조회 (N+1 문제 해결)
     * @return 모든 회원의 언어 관심도 목록
     */
    public List<String> getAllMemberTopLangs() {
        log.info("getAllMemberTopLangs 호출됨");
        // 모든 활성 회원의 언어 관심도를 한 번에 조회
        List<String> allLangs = memberInterestRepository.findAllActiveMemberTopLangs();
        log.info("전체 회원 언어 관심도 조회 완료 - 총 {}개", allLangs.size());
        return allLangs;
    }

    /**
     * 사용자의 상위 6개 언어 관심도 점수를 조회합니다.
     * 관심도 점수는 해당 언어를 선택한 전체 사용자 수를 기준으로 계산됩니다.
     * @param email 사용자 이메일
     * @return 상위 6개 언어의 관심도 점수 리스트 (0-100 사이의 정수)
     */
    public List<Integer> getTop6LangScores(String email) {
        log.info("getTop6LangScores 호출됨 - email: {}", email);
        Member member = memberService.findByEmail(email);
        List<String> top6Langs = memberInterestRepository.findTop6SkillsByUserId(member.getUserId());
        log.info("조회된 상위 6개 언어: {}", top6Langs);
        
        long totalUsers = memberService.countActiveUsers();
        log.info("전체 활성 사용자 수: {}", totalUsers);
        
        List<Integer> scores = top6Langs.stream()
            .map(lang -> {
                Integer count = memberInterestRepository.countByInterestName(lang);
                log.info("언어 '{}' 선택한 사용자 수: {}", lang, count);
                if (count == null || totalUsers == 0) return 0;
                int score = (int) ((double) count / totalUsers * 100);
                log.info("언어 '{}'의 점수: {}", lang, score);
                return score;
            })
            .collect(Collectors.toList());
        
        log.info("최종 점수 목록: {}", scores);
        return scores;
    }
}
