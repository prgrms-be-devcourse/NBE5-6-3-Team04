package com.grepp.nbe562team04.model.ai.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {

    private String message;
    private Role role;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate timestamp;
}

