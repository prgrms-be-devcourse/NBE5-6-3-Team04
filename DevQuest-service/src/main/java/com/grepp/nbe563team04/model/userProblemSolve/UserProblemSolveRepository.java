package com.grepp.nbe563team04.model.userProblemSolve;

import com.grepp.nbe563team04.model.userProblemSolve.entity.UserProblemId;
import com.grepp.nbe563team04.model.userProblemSolve.entity.UserProblemSolve;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProblemSolveRepository extends JpaRepository<UserProblemSolve, UserProblemId> {
}
