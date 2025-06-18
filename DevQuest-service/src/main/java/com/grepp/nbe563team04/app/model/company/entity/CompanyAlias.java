package com.grepp.nbe563team04.app.model.company.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "company_aliases")
public class CompanyAlias {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 기업별칭 (예: TOSS, 토스)
    @Column(name = "alias_name", nullable = false)
    private String aliasName;

    // 정규화된 기업과의 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "normalized_company_id", nullable = false)
    private NormalizedCompany normalizedCompany;

    public CompanyAlias(String aliasName, NormalizedCompany normalizedCompany) {
        this.aliasName = aliasName;
        this.normalizedCompany = normalizedCompany;
    }
} 