package com.grepp.nbe563team04.model.auth.domain;

import com.grepp.nbe563team04.model.member.entity.Member;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class Principal implements UserDetails {

    private final Member member;

    public Principal(Member member) {
        this.member = member;
    }

    public Member getUser() {
        return this.member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한 목록 리턴 (예: ROLE_USER 등)
        return List.of(() -> member.getRole().name());
    }

    public static Principal createPrincipal(Member member, List<SimpleGrantedAuthority> authorities) {
        return new Principal(member);
//        return new Principal(member.getEmail(), member.getPassword(), authorities);
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getEmail(); // 혹은 ID
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

