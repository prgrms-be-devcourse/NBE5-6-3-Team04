package com.grepp.nbe563team04.app.model.member;

import com.grepp.nbe563team04.app.model.member.entity.MembersAchieve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MembersAchieveRepository extends JpaRepository<MembersAchieve, Long> {
    boolean existsByMember_UserIdAndAchievement_AchieveId(Long userId, Long achieveId);

    // 최근 3개 업적 조회
    List<MembersAchieve> findTop3ByMember_UserIdOrderByAchievedAtDesc(Long userId);
    // 전체 업적 조회
    List<MembersAchieve> findByMember_UserIdOrderByAchievedAtDesc(Long userId);

    @Query("SELECT ma FROM MembersAchieve ma JOIN FETCH ma.achievement WHERE ma.member.userId = :userId")
    List<MembersAchieve> findWithAchievementByUserId(@Param("userId") Long userId);

    @Query("SELECT ma.achievement.achieveId FROM MembersAchieve ma WHERE ma.member.userId = :userId")
    List<Long> findAchievedIdsByUserId(@Param("userId") Long userId);
}