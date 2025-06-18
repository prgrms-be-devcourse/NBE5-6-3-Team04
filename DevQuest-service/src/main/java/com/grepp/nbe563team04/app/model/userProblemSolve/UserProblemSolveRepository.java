package com.grepp.nbe563team04.app.model.userProblemSolve;

import com.grepp.nbe563team04.app.model.userProblemSolve.entity.UserProblemId;
import com.grepp.nbe563team04.app.model.userProblemSolve.entity.UserProblemSolve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserProblemSolveRepository extends JpaRepository<UserProblemSolve, UserProblemId> {
//    기본적인 CRUD
//    findById(id)	    SELECT * FROM table WHERE id = ? // 테이블 명은 만들어둔 entity 값을 보고 판단, id는 엔티티에서의 기본키를 의미
//    findAll()	        SELECT * FROM table
//    save(entity)	    INSERT 또는 UPDATE
//    deleteById(id)	DELETE FROM table WHERE id = ?
//    existsById(id)	존재 여부 확인
    @Query("SELECT userProblemSolve.id.problemId, userProblemSolve.solveCount " +
            "FROM UserProblemSolve userProblemSolve " +
            "WHERE userProblemSolve.id.userId = :userId")
    List<Object[]> findSolvedCountsByUserId(@Param("userId") Long userId);


}
