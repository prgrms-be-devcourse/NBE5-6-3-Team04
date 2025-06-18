package com.grepp.nbe563team04.app.model.company;

import com.grepp.nbe563team04.app.model.company.entity.CompanyAlias;
import com.grepp.nbe563team04.app.model.company.entity.NormalizedCompany;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyAliasService {
    private final CompanyAliasRepository companyAliasRepository;
    private final NormalizedCompanyRepository normalizedCompanyRepository;

    public List<CompanyAlias> findAll() {
        return companyAliasRepository.findAll();
    }

    @Transactional
    public void addAlias(String alias, Long normalizedCompanyId) {
        NormalizedCompany company = normalizedCompanyRepository.findById(normalizedCompanyId)
            .orElseThrow(() -> new IllegalArgumentException("표준 기업명 없음"));
        CompanyAlias companyAlias = new CompanyAlias();
        companyAlias.setAliasName(alias);
        companyAlias.setNormalizedCompany(company);
        companyAliasRepository.save(companyAlias);
    }

    @Transactional
    public void editAlias(Long aliasId, String alias, Long normalizedCompanyId) {
        CompanyAlias companyAlias = companyAliasRepository.findById(aliasId)
            .orElseThrow(() -> new IllegalArgumentException("별칭 없음"));
        NormalizedCompany company = normalizedCompanyRepository.findById(normalizedCompanyId)
            .orElseThrow(() -> new IllegalArgumentException("표준 기업명 없음"));
        companyAlias.setAliasName(alias);
        companyAlias.setNormalizedCompany(company);
        companyAliasRepository.save(companyAlias);
    }

    @Transactional
    public void deleteAlias(Long aliasId) {
        companyAliasRepository.deleteById(aliasId);
    }
} 