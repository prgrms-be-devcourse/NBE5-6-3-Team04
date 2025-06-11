package com.grepp.nbe562team04.model.level;

import com.grepp.nbe562team04.model.level.entity.Level;
import com.grepp.nbe562team04.model.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LevelService {

    private final LevelRepository levelRepository;

    public int levelProgress(User user){
        int userExp = user.getExp();
        int currentXp = user.getLevel().getXp();

        Optional<Level> nextLevelOpt = levelRepository.findTopByXpGreaterThanOrderByXpAsc(userExp);
        int nextXp = nextLevelOpt.map(Level::getXp).orElse(currentXp + 1);

        int progress = 0;
        if (nextXp > currentXp) {
            progress = (int)(((double)(userExp - currentXp) / (nextXp - currentXp)) * 100);
        }
        progress = Math.max(0, Math.min(progress, 100));

        return progress;
    }
}
