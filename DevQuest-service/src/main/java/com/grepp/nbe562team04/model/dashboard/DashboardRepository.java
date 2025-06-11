package com.grepp.nbe562team04.model.dashboard;

import com.grepp.nbe562team04.model.goalcompany.entity.GoalCompany;
import com.grepp.nbe562team04.model.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DashboardRepository extends JpaRepository<GoalCompany, Long> {

    // 특정 유저의 목표 기업 목록 조회
    List<GoalCompany> findGoalCompaniesByUser(User user);

    // ID로 목표 기업 상세 조회
    Optional<GoalCompany> findByCompanyId(Long companyId);
}