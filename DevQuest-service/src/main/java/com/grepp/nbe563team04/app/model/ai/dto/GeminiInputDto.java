package com.grepp.nbe563team04.app.model.ai.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class GeminiInputDto {
    private String prompt;
    private String personality; //  kind or rude
}
