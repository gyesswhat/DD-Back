package com.example.ddback.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해 CORS 허용 설정
                .allowedOrigins("https://2024-ewha-unis-festival-booth.vercel.app/", "http://localhost:3000/"); // 허용할 origin 목록
    }
}
