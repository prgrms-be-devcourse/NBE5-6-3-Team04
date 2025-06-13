package com.grepp.nbe563team04.model.user;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserInterestService {

    private final UserInterestRepository userInterestRepository;

    public UserInterestService(UserInterestRepository userInterestRepository) {
        this.userInterestRepository = userInterestRepository;
    }

    public List<String> getTop6Langs(String userId) {
        return userInterestRepository.findTop6ByUserIdAndType(userId);
    }
}
