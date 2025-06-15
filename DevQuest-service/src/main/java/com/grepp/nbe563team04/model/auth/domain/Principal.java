package com.grepp.nbe563team04.model.auth.domain;

import com.grepp.nbe563team04.model.member.entity.Member;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Getter
public class Principal extends User {

    private final Member member;

    public Principal(String email, String password,
        Collection<? extends GrantedAuthority> authorities) {
        super(email, password, authorities);
        this.member = null;
    }

    public Principal(Member member) {
        super(member.getEmail(), member.getPassword(),
            List.of(new SimpleGrantedAuthority(member.getRole().name())));
        this.member = member;
    }

    public static Principal createPrincipal(Member member,
        List<SimpleGrantedAuthority> authorities){
        return new Principal(member.getEmail(), member.getPassword(), authorities);
    }
}