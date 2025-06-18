package com.grepp.nbe563team04.app.model.dashboard;

import com.grepp.nbe563team04.app.model.goalcompany.entity.GoalCompany;
import com.grepp.nbe563team04.app.model.member.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DashboardRepository extends JpaRepository<GoalCompany, Long> {

    // 특정 유저의 목표 기업 목록 조회
    @Query("SELECT gc FROM GoalCompany gc JOIN FETCH gc.normalizedCompany WHERE gc.member = :member")
    List<GoalCompany> findGoalCompaniesByMember(@Param("member") Member member);

    // ID로 목표 기업 상세 조회
    Optional<GoalCompany> findByCompanyId(Long companyId);

}