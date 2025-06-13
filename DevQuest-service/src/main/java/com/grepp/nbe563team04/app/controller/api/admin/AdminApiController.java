package com.grepp.nbe563team04.app.controller.api.admin;

import com.grepp.nbe563team04.model.admin.CompanyService;
import com.grepp.nbe563team04.model.admin.dto.CompanyStatsDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminApiController {

    private final CompanyService companyService;

    @GetMapping("/company-stats")
    public List<CompanyStatsDto> getCompanyStats() {
        return companyService.getCompanyStats();
    }

}
