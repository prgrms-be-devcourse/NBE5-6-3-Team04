package com.grepp.nbe563team04.app.model.goal;

import com.grepp.nbe563team04.app.model.goal.entity.Goal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GoalRepository extends JpaRepository<Goal, Long> {
//    기본적인 CRUD
//    findById(id)	    SELECT * FROM table WHERE id = ? // 테이블 명은 만들어둔 entity 값을 보고 판단, id는 엔티티에서의 기본키를 의미
//    findAll()	        SELECT * FROM table
//    save(entity)	    INSERT 또는 UPDATE
//    deleteById(id)	DELETE FROM table WHERE id = ?
//    existsById(id)	존재 여부 확인

    List<Goal> findByCompanyCompanyId(Long companyId);// 특정 기업의 목표 리스트 조회용

    @Query("SELECT g.color FROM Goal g WHERE g.company.companyId = :companyId")
    List<String> findColorsByCompanyId(@Param("companyId") Long companyId);
}
