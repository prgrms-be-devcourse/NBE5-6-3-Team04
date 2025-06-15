package com.grepp.nbe563team04.infra.auth.token.filter;

import com.grepp.nbe563team04.infra.auth.token.JwtProvider;
import com.grepp.nbe563team04.infra.auth.token.TokenCookieFactory;
import com.grepp.nbe563team04.infra.auth.token.code.TokenType;
import com.grepp.nbe563team04.infra.error.exceptions.CommonException;
import com.grepp.nbe563team04.infra.response.ResponseCode;
import com.grepp.nbe563team04.model.auth.token.RefreshTokenService;
import com.grepp.nbe563team04.model.auth.token.UserBlackListRepository;
import com.grepp.nbe563team04.model.auth.token.dto.AccessTokenDto;
import com.grepp.nbe563team04.model.auth.token.entity.RefreshToken;
import com.grepp.nbe563team04.model.auth.token.entity.UserBlackList;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserBlackListRepository userBlackListRepository;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        List<String> excludePaths = List.of(
            "/error", "/favicon.ico", "/css", "/img", "/js", "/download",
            "/member/signup"
        );
        String path = request.getRequestURI();
        return excludePaths.stream().anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String requestAccessToken = jwtProvider.resolveToken(request, TokenType.ACCESS_TOKEN);
        if (requestAccessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        Claims claims = jwtProvider.parseClaim(requestAccessToken);
        if(userBlackListRepository.existsById(claims.getSubject())){
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (jwtProvider.validateToken(requestAccessToken)) {
                Authentication authentication = jwtProvider.generateAuthentication(requestAccessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (ExpiredJwtException ex) {
            AccessTokenDto newAccessToken = renewingAccessToken(requestAccessToken, request);
            if (newAccessToken == null) {
                filterChain.doFilter(request, response);
                return;
            }

            RefreshToken newRefreshToken = renewingRefreshToken(ex.getClaims().getId(), newAccessToken.getId());
            responseToken(response, newAccessToken, newRefreshToken);
        }

        filterChain.doFilter(request, response);
    }

    private AccessTokenDto renewingAccessToken(String accessToken, HttpServletRequest request) {
        Authentication authentication = jwtProvider.generateAuthentication(accessToken);
        String refreshToken = jwtProvider.resolveToken(request, TokenType.REFRESH_TOKEN);
        Claims claims = jwtProvider.parseClaim(accessToken);

        RefreshToken storedRefreshToken = refreshTokenService.findByAccessTokenId(claims.getId());

        if(storedRefreshToken == null) {
            return null;
        }

        if (!storedRefreshToken.getToken().equals(refreshToken)) {
            userBlackListRepository.save(new UserBlackList(authentication.getName()));
            throw new CommonException(ResponseCode.SECURITY_INCIDENT);
        }

        AccessTokenDto newAccessToken = jwtProvider.generateAccessToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return newAccessToken;
    }

    private RefreshToken renewingRefreshToken(String oldAccessTokenId, String newAccessTokenId) {
        return refreshTokenService.renewingToken(oldAccessTokenId, newAccessTokenId);
    }

    private void responseToken(HttpServletResponse response, AccessTokenDto at, RefreshToken rt) {
        ResponseCookie accessTokenCookie = TokenCookieFactory.create(
            TokenType.ACCESS_TOKEN.name(), at.getToken(), at.getExpiresIn()
        );
        ResponseCookie refreshTokenCookie = TokenCookieFactory.create(
            TokenType.REFRESH_TOKEN.name(), rt.getToken(), jwtProvider.getRtExpiration()
        );

        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
    }
}
