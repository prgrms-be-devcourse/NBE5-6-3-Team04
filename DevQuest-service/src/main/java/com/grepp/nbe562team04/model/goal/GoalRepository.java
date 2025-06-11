package com.grepp.nbe562team04.model.goal;

import com.grepp.nbe562team04.model.goal.entity.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findByCompanyCompanyId(Long companyId);// 특정 기업의 목표 리스트 조회용
    long countByCompany_User_UserId(Long userId);

}
