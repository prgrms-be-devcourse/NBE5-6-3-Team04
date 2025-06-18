package com.grepp.nbe563team04.app.model.company;

import com.grepp.nbe563team04.app.model.company.entity.NormalizedCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NormalizedCompanyRepository extends JpaRepository<NormalizedCompany, Long> {
    Optional<NormalizedCompany> findByNormalizedName(String normalizedName);

    Optional<NormalizedCompany> findByStandardName(String standardName);
} 