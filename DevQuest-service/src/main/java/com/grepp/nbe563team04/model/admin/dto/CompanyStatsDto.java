package com.grepp.nbe563team04.model.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CompanyStatsDto {
    private String company;
    private int projectCount;
}
