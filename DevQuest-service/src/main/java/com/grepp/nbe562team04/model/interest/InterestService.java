package com.grepp.nbe562team04.model.interest;

import com.grepp.nbe562team04.model.interest.dto.InterestDto;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class InterestService {

    private final InterestRepository interestRepository;

    public Map<String, List<InterestDto>> findInterestGroupByType() {
        List<InterestDto> interests = Optional.of(interestRepository.findAll())
            .orElse(Collections.emptyList()).stream()
            .map(InterestDto::new)
            .toList();

        List<InterestDto> interestRoles = interests.stream()
            .filter(interest -> interest.getType().name().equals("ROLE"))
            .collect(Collectors.toList());

        List<InterestDto> interestSkills = interests.stream()
            .filter(interest -> interest.getType().name().equals("SKILL"))
            .collect(Collectors.toList());

        Map<String, List<InterestDto>> result = new HashMap<>();
        result.put("interestRoles", interestRoles);
        result.put("interestSkills", interestSkills);

        return result;
    }
}
