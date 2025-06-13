package com.grepp.nbe563team04.model.goal;

import com.grepp.nbe563team04.model.goal.entity.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findByCompanyCompanyId(Long companyId);// 특정 기업의 목표 리스트 조회용

    @Query("SELECT g.color FROM Goal g WHERE g.company.companyId = :companyId")
    List<String> findColorsByCompanyId(@Param("companyId") Long companyId);
}
