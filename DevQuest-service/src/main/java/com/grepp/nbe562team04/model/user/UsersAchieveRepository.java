package com.grepp.nbe562team04.model.user;

import com.grepp.nbe562team04.model.user.entity.UsersAchieve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersAchieveRepository extends JpaRepository<UsersAchieve, Long> {
    boolean existsByUser_UserIdAndAchievement_AchieveId(Long userId, Long achieveId);

    // 최근 3개 업적 조회
    List<UsersAchieve> findTop3ByUser_UserIdOrderByAchievedAtDesc(Long userId);
    // 전체 업적 조회
    List<UsersAchieve> findByUser_UserIdOrderByAchievedAtDesc(Long userId);

    @Query("SELECT ua FROM UsersAchieve ua JOIN FETCH ua.achievement WHERE ua.user.userId = :userId")
    List<UsersAchieve> findWithAchievementByUserId(@Param("userId") Long userId);
}