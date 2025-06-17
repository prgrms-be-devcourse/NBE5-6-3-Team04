package com.grepp.nbe563team04;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Nbe563Team04Application {

    public static void main(String[] args) {
        // .env 파일 로드
        Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()  // .env 파일이 없어도 예외 발생하지 않도록
            .load();

        // 시스템 환경변수 등록
        System.setProperty("UPLOAD_PATH", dotenv.get("UPLOAD_PATH"));
        System.setProperty("ACHIEVEMENT_UPLOAD_PATH", dotenv.get("ACHIEVEMENT_UPLOAD_PATH"));
        System.setProperty("DB_URL", dotenv.get("DB_URL"));
        System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
        System.setProperty("GEMINI_BASE_URL", dotenv.get("GEMINI_BASE_URL"));
        System.setProperty("GEMINI_API_KEY", dotenv.get("GEMINI_API_KEY"));
        System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
        System.setProperty("JWT_ACCESS_EXPIRATION", dotenv.get("JWT_ACCESS_EXPIRATION"));
        System.setProperty("JWT_REFRESH_EXPIRATION", dotenv.get("JWT_REFRESH_EXPIRATION"));

        SpringApplication.run(Nbe563Team04Application.class, args);
    }
}
