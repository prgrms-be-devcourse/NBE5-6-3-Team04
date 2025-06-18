package com.grepp.nbe563team04.app.model.achievement;

import com.grepp.nbe563team04.app.model.achievement.entity.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AchieveRepository extends JpaRepository<Achievement, Long> {
    Achievement findByName(String name);
    List<Achievement> findAll();
}