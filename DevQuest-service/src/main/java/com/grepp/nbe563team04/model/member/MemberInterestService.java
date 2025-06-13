package com.grepp.nbe563team04.model.member;

import com.grepp.nbe563team04.model.member.entity.Member;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MemberInterestService {

    private final MemberInterestRepository memberInterestRepository;
    private final MemberService memberService;

    public MemberInterestService(
        MemberInterestRepository memberInterestRepository, MemberService memberService) {
        this.memberInterestRepository = memberInterestRepository;
        this.memberService = memberService;
    }

    public List<String> getTop6Langs(String email) {
        Member member = memberService.findByEmail(email);
        return memberInterestRepository.findTop6ByUserIdAndType(member.getUserId());
    }
}
