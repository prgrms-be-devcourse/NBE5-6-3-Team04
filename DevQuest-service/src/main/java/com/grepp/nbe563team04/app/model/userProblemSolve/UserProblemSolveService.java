package com.grepp.nbe563team04.app.model.userProblemSolve;

import com.grepp.nbe563team04.app.model.member.entity.Member;
import com.grepp.nbe563team04.app.model.member.MemberRepository;
import com.grepp.nbe563team04.app.model.problem.entity.Problem;
import com.grepp.nbe563team04.app.model.problem.ProblemRepository;
import com.grepp.nbe563team04.app.model.userProblemSolve.entity.UserProblemId;
import com.grepp.nbe563team04.app.model.userProblemSolve.entity.UserProblemSolve;
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
        Member user = memberRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        for (Long problemId : problemIds) {
            Problem problem = problemRepository.findById(problemId)
                    .orElseThrow(() -> new RuntimeException("문제를 찾을 수 없습니다."));

            UserProblemId id = new UserProblemId(userId, problemId);

            // 이미 존재하면 패스 (중복 저장 방지)
            if (userProblemSolveRepository.existsById(id)) {
                continue;
            }

            // solveCount = 0 으로 저장
            UserProblemSolve newSolve = new UserProblemSolve(id, user, problem, 0);
            userProblemSolveRepository.save(newSolve);
        }
    }

}

