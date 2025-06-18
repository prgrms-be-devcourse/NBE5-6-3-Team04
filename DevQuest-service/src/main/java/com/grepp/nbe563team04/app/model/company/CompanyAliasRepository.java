package com.grepp.nbe563team04.app.model.company;

import com.grepp.nbe563team04.app.model.company.entity.CompanyAlias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyAliasRepository extends JpaRepository<CompanyAlias, Long> {
    Optional<CompanyAlias> findByAliasName(String aliasName);
} 