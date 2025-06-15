package com.grepp.nbe563team04.infra.auth;

import com.grepp.nbe563team04.model.auth.domain.Principal;
import com.grepp.nbe563team04.model.member.MemberRepository;
import com.grepp.nbe563team04.model.member.entity.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    // 로그인 사용자 확인 및 권한 설정
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException(email));

        // member 객체에서 직접 권한 추출
        List<SimpleGrantedAuthority> authorities = List.of(
            new SimpleGrantedAuthority(member.getRole().name())
        );

        return Principal.createPrincipal(member, authorities);
    }

    /**
     * JwtProvider에서만 필요한 경우 사용할 수 있는 권한 조회 메서드
     */
    // JWT 토큰이 의미하는 사용자가 가지고 있는 권한 조회
    @Cacheable("user-authorities")
    public List<SimpleGrantedAuthority> findAuthorities(String email) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException(email));

        return List.of(new SimpleGrantedAuthority(member.getRole().name()));
    }
}
