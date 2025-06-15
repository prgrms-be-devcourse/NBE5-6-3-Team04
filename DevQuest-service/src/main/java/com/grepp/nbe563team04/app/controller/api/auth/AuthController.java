package com.grepp.nbe563team04.app.controller.api.auth;

import com.grepp.nbe563team04.app.controller.api.auth.payload.SigninRequest;
import com.grepp.nbe563team04.app.controller.api.auth.payload.TokenResponse;
import com.grepp.nbe563team04.infra.auth.token.TokenCookieFactory;
import com.grepp.nbe563team04.infra.auth.token.code.GrantType;
import com.grepp.nbe563team04.infra.auth.token.code.TokenType;
import com.grepp.nbe563team04.infra.response.ApiResponse;
import com.grepp.nbe563team04.model.auth.AuthService;
import com.grepp.nbe563team04.model.auth.token.dto.TokenResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("signin")
    public ResponseEntity<ApiResponse<TokenResponse>> signin(
        @RequestBody
        SigninRequest req,
        HttpServletResponse response
    ) {
        TokenResponseDto dto = authService.signin(req.getEmail(), req.getPassword());
        ResponseCookie accessTokenCookie = TokenCookieFactory.create(TokenType.ACCESS_TOKEN.name(),
            dto.getAccessToken(), dto.getAtExpiresIn());
        ResponseCookie refreshTokenCookie = TokenCookieFactory.create(TokenType.REFRESH_TOKEN.name(),
            dto.getRefreshToken(), dto.getRtExpiresIn());

        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
        TokenResponse tokenResponse = TokenResponse.builder()
            .accessToken(dto.getAccessToken())
            .expiresIn(dto.getAtExpiresIn())
            .grantType(GrantType.BEARER)
            .build();
        return ResponseEntity.ok(ApiResponse.success(tokenResponse));
    }

    @PostMapping("logout")
    public ResponseEntity<ApiResponse<Void>> logout(
        HttpServletResponse response
    ){
        return ResponseEntity.ok(ApiResponse.noContent());
    }
}