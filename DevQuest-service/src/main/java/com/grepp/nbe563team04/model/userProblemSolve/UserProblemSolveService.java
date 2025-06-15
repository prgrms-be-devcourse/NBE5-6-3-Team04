package com.grepp.nbe563team04.model.userProblemSolve;

import com.grepp.nbe563team04.model.member.entity.Member;
import com.grepp.nbe563team04.model.member.MemberRepository;
import com.grepp.nbe563team04.model.problem.entity.Problem;
import com.grepp.nbe563team04.model.problem.ProblemRepository;
import com.grepp.nbe563team04.model.userProblemSolve.entity.UserProblemId;
import com.grepp.nbe563team04.model.userProblemSolve.entity.UserProblemSolve;
import com.grepp.nbe563team04.model.userProblemSolve.UserProblemSolveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProblemSolveService {

    private final UserProblemSolveRepository userProblemSolveRepository;
    private final MemberRepository memberRepository;
    private final ProblemRepository problemRepository;

    @Transactional
    public void saveAll(Long userId, List<Long> problemIds) {
        Member user = memberRepository.findById(userId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        for (Long problemId : problemIds) {
            Problem problem = problemRepository.findById(problemId).orElseThrow(() -> new RuntimeException("문제를 찾을 수 없습니다."));

            UserProblemId id = new UserProblemId(userId, problemId);

            UserProblemSolve entity = userProblemSolveRepository.findById(id)
                    .map(existing -> {
                        existing.setSolveCount(existing.getSolveCount() + 1);
                        return existing;
                    })
                    .orElseGet(() -> new UserProblemSolve(id, user, problem, 1));

            userProblemSolveRepository.save(entity);
        }
    }
}

