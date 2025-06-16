package com.grepp.nbe563team04.model.auth;

import com.grepp.nbe563team04.infra.auth.token.JwtProvider;
import com.grepp.nbe563team04.infra.auth.token.code.GrantType;
import com.grepp.nbe563team04.model.auth.token.RefreshTokenRepository;
import com.grepp.nbe563team04.model.auth.token.UserBlackListRepository;
import com.grepp.nbe563team04.model.auth.token.dto.AccessTokenDto;
import com.grepp.nbe563team04.model.auth.token.dto.TokenResponseDto;
import com.grepp.nbe563team04.model.auth.token.entity.RefreshToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final UserBlackListRepository userBlackListRepository;
    private final JwtProvider jwtProvider;

    /**
     * 일반 로그인 처리 (이메일/비밀번호 검증 후 토큰 발급)
     * 1. 사용자 인증 단계
     */
    public TokenResponseDto signin(String email, String password){
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);
        // loadUserByUsername + password 검증 후 authentication 반환
        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return processTokenSignin(email);
    }


    /**
     * JWT 토큰 발급 및 RefreshToken 저장
     * 2. 인증된 사용자에게 JWT 토큰 발급
     */
    public TokenResponseDto processTokenSignin(String username) {
        // 블랙리스트에서 제거
        userBlackListRepository.deleteById(username);

        AccessTokenDto dto = jwtProvider.generateAccessToken(username);
        RefreshToken refreshToken = new RefreshToken(username, dto.getId());
        refreshTokenRepository.save(refreshToken);

        return TokenResponseDto.builder()
            .accessToken(dto.getToken())
            .refreshToken(refreshToken.getToken())
            .atExpiresIn(jwtProvider.getAtExpiration())
            .rtExpiresIn(jwtProvider.getRtExpiration())
            .grantType(GrantType.BEARER)
            .build();
    }
}