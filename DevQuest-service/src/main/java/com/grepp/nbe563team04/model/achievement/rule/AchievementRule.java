package com.grepp.nbe563team04.model.achievement.rule;

import com.grepp.nbe563team04.model.user.entity.User;

public interface AchievementRule {
    Long getAchievementId();
    boolean isSatisfied(User user);
}
