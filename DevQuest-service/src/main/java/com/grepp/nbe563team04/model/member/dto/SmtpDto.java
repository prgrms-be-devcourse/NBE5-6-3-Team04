package com.grepp.nbe563team04.model.member.dto;

import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SmtpDto {
    private String from;
    private String subject;
    private List<String> to;
    private Map<String, String> properties;
    private String eventType;
}
