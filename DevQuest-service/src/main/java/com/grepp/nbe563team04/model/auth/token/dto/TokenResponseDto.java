package com.grepp.nbe563team04.model.auth.token.dto;

import com.grepp.nbe563team04.infra.auth.token.code.GrantType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenResponseDto {
    private String accessToken;
    private String refreshToken;
    private GrantType grantType;
    private Long atExpiresIn;
    private Long rtExpiresIn;
}
