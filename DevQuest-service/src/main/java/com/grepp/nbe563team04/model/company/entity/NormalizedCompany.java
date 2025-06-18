package com.grepp.nbe563team04.model.company.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "normalized_companies")
public class NormalizedCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 정규화된 기업명 (예: toss)
    @Column(name = "normalized_name", unique = true, nullable = false)
    private String normalizedName;

    // 표준 기업명 (예: 토스)
    @Column(nullable = false, unique = true)
    private String standardName;

    // 기업 로고 URL
    @Column(name = "logo_url")
    private String logoUrl;

    // 기업 색상 (예: #512da8)
    @Column(name = "color")
    private String color;

    // 기업별칭 목록 (JSON 변환 시 순환참조 방지)
    @OneToMany(mappedBy = "normalizedCompany", cascade = CascadeType.ALL, orphanRemoval = true)
    @com.fasterxml.jackson.annotation.JsonIgnore // 순환참조 방지용
    private List<CompanyAlias> aliases = new ArrayList<>();

    public NormalizedCompany(String normalizedName, String standardName) {
        this.normalizedName = normalizedName;
        this.standardName = standardName;
    }

    // 별칭 추가 메서드
    public void addAlias(CompanyAlias alias) {
        aliases.add(alias);
        alias.setNormalizedCompany(this);
    }
} 