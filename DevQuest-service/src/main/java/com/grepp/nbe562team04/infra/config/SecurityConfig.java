package com.grepp.nbe562team04.infra.config;

import com.grepp.nbe562team04.model.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${remember-me.key}")
    private String rememberMeKey;

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {

            boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin) {
                response.sendRedirect("/admin/dashboard");
                return;
            }
            response.sendRedirect("/dashboard");
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            Authentication auth = (Authentication) request.getUserPrincipal();

            if (auth != null) {
                boolean isAdmin = auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(role -> role.equals("ROLE_ADMIN"));

                if (isAdmin) {
                    response.sendRedirect("/admin/dashboard");
                } else {
                    response.sendRedirect("/dashboard");
                }
            } else {
                response.sendRedirect("/signin");
            }
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UserService userService)
        throws Exception {
        http.userDetailsService(userService)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/css/**", "/js/**", "/img/**", "/api/user/exists/*",
                                "/user/interests").permitAll() // 모두에게 허용
                        .requestMatchers("/", "/serviceInfo", "/signin", "/signup", "/admin/signup")
                        .anonymous() // 회원가입, 로그인 접근 권한
                        .requestMatchers("/user/withdraw-success").permitAll()
                        .requestMatchers("/admin/dashboard").hasRole("ADMIN") // 관리자페이지 접근 권한
                        .requestMatchers("/user/**", "/dashboard/**", "/api/dashboard/**",
                                "/todos/**", "/companies/**", "/goals/**", "/images/profile/**","/api/ai/feedback").hasRole("USER") // 사용자페이지 접근 권한
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/signin")
                        .usernameParameter("email")
                        .loginProcessingUrl("/user/signin")
                        .successHandler(successHandler())
                )
                .rememberMe(rememberMe -> rememberMe
                        .key(rememberMeKey).rememberMeParameter("remember-me")
                        .userDetailsService(userService)
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(accessDeniedHandler())
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/signin")
                        .invalidateHttpSession(true) // 세션무효화
                        .deleteCookies("JSESSIONID")  // 쿠키 삭제
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return userService;
    }


}