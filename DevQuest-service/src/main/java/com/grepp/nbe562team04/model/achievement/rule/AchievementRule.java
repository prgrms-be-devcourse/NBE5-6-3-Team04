package com.grepp.nbe562team04.model.achievement.rule;

import com.grepp.nbe562team04.model.user.entity.User;

public interface AchievementRule {
    Long getAchievementId();
    boolean isSatisfied(User user);
}
