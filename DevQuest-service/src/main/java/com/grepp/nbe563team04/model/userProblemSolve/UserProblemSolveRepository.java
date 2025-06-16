package com.grepp.nbe563team04.model.userProblemSolve;

import com.grepp.nbe563team04.model.userProblemSolve.entity.UserProblemId;
import com.grepp.nbe563team04.model.userProblemSolve.entity.UserProblemSolve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserProblemSolveRepository extends JpaRepository<UserProblemSolve, UserProblemId> {

    @Query("SELECT userProblemSolve.id.problemId, userProblemSolve.solveCount " +
            "FROM UserProblemSolve userProblemSolve " +
            "WHERE userProblemSolve.id.userId = :userId")
    List<Object[]> findSolvedCountsByUserId(@Param("userId") Long userId);


}
