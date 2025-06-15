package com.grepp.nbe563team04.infra.auth.token;

import com.grepp.nbe563team04.infra.auth.token.code.TokenType;
import com.grepp.nbe563team04.model.auth.domain.Principal;
import com.grepp.nbe563team04.model.auth.token.dto.AccessTokenDto;
import com.grepp.nbe563team04.model.member.MemberRepository;
import com.grepp.nbe563team04.model.member.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {

    private final MemberRepository memberRepository;

    @Value("${jwt.secret}")
    private String key;

    @Getter
    @Value("${jwt.access-expiration}")
    private long atExpiration;

    @Getter
    @Value("${jwt.refresh-expiration}")
    private long rtExpiration;

    private SecretKey secretKey;

    public SecretKey getSecretKey(){
        if(secretKey == null){
            String base64Key = Base64.getEncoder().encodeToString(key.getBytes());
            secretKey = Keys.hmacShaKeyFor(base64Key.getBytes(StandardCharsets.UTF_8));
        }
        return secretKey;
    }

    public AccessTokenDto generateAccessToken(Authentication authentication){
        return generateAccessToken(authentication.getName());
    }

    public AccessTokenDto generateAccessToken(String email){
        String id = UUID.randomUUID().toString();
        long now = new Date().getTime();
        Date atExpiresIn = new Date(now + atExpiration);

        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        List<String> authorities = List.of(member.getRole().name());

        String accessToken = Jwts.builder()
            .subject(email)
            .id(id)
            .expiration(atExpiresIn)
            .claim("authorities", authorities)
            .signWith(getSecretKey())
            .compact();

        return AccessTokenDto.builder()
            .id(id)
            .token(accessToken)
            .expiresIn(atExpiration)
            .build();
    }

    public Authentication generateAuthentication(String accessToken) {
        String email = parseClaim(accessToken).getSubject();

        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        List<SimpleGrantedAuthority> authorities =
            List.of(new SimpleGrantedAuthority(member.getRole().name()));

        Principal principal = new Principal(member);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public Claims parseClaim(String accessToken) {
        try{
            return Jwts.parser().verifyWith(getSecretKey()).build()
                .parseSignedClaims(accessToken).getPayload();
        }catch (ExpiredJwtException ex){
            return ex.getClaims();
        }
    }


    public boolean validateToken(String requestAccessToken) {
        try{
            Jwts.parser().verifyWith(getSecretKey()).build().parse(requestAccessToken);
            return true;
        }catch(SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e){
            log.error(e.getMessage(), e);
        }
        return false;
    }

    public String resolveToken(HttpServletRequest request, TokenType tokenType) {
        String headerToken = request.getHeader("Authorization");
        if (headerToken != null && headerToken.startsWith("Bearer")) {
            return headerToken.substring(7);
        }

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        return Arrays.stream(cookies)
            .filter(e -> e.getName().equals(tokenType.name()))
            .map(Cookie::getValue).findFirst()
            .orElse(null);
    }
}
