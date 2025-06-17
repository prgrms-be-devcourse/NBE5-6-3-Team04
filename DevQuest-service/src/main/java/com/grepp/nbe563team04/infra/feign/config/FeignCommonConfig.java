package com.grepp.nbe563team04.infra.feign.config;

import com.grepp.nbe563team04.infra.auth.token.JwtProvider;
import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class FeignCommonConfig {

    private final JwtProvider jwtProvider;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            String serverToken = jwtProvider.generateServerToken(); // 동적으로 생성

            requestTemplate.header("Authorization", "Bearer " + serverToken);
        };
    }
}


