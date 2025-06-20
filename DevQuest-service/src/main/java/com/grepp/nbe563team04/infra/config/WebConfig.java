package com.grepp.nbe563team04.infra.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${upload.path}")
    private String uploadPath;

    @Value("${upload.achievement-path}")
    private String achievementPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/profile/**")
                .addResourceLocations("file:" + uploadPath + "/");

        registry.addResourceHandler("/upload/achievement/**")
                .addResourceLocations("file:" + achievementPath + "/");
    }
}