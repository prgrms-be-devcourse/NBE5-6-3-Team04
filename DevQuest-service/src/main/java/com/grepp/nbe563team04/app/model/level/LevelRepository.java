package com.grepp.nbe563team04.app.model.level;

import com.grepp.nbe563team04.app.model.level.entity.Level;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LevelRepository extends JpaRepository<Level, Long> {
    Optional<Level> findFirstByOrderByLevelIdAsc();
    // 현재 레벨 기준 XP 이하 중 가장 높은 레벨
    Optional<Level> findTopByXpLessThanEqualOrderByXpDesc(int exp);
    // 다음 레벨 기준 XP 초과 중 가장 낮은 레벨
    Optional<Level> findTopByXpGreaterThanOrderByXpAsc(int exp);

}