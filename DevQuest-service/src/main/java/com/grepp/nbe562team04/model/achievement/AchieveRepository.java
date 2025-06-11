package com.grepp.nbe562team04.model.achievement;

import com.grepp.nbe562team04.model.achievement.entity.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AchieveRepository extends JpaRepository<Achievement, Long> {
    Achievement findByName(String name);
}