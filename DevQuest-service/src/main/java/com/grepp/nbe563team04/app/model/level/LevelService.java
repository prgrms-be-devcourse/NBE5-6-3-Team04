package com.grepp.nbe563team04.app.model.level;

import com.grepp.nbe563team04.app.model.level.entity.Level;
import com.grepp.nbe563team04.app.model.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LevelService {

    private final LevelRepository levelRepository;

    public int levelProgress(Member member){
        int userExp = member.getExp();
        int currentXp = member.getLevel().getXp();

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
