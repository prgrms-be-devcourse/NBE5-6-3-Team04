package com.grepp.nbe563team04.model.admin;

import com.grepp.nbe563team04.model.admin.dto.CompanyStatsDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public List<CompanyStatsDto> getCompanyStats() {
        return companyRepository.findAll().stream()
            .map(company -> new CompanyStatsDto(
                company.getName(),
                company.getProjects().size()
            ))
            .collect(Collectors.toList());
    }
}