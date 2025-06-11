package com.grepp.nbe562team04.model.auth.domain;

import com.grepp.nbe562team04.model.user.entity.User;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class Principal implements UserDetails {

    private final User user;

    public Principal(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한 목록 리턴 (예: ROLE_USER 등)
        return List.of(() -> user.getRole().name());
    }

    public static Principal createPrincipal(User member, List<SimpleGrantedAuthority> authorities) {
        return new Principal(member);
//        return new Principal(member.getEmail(), member.getPassword(), authorities);
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // 혹은 ID
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

