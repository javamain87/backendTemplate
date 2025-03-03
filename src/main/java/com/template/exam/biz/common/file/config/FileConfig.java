package com.template.exam.biz.common.file.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class FileConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    /*
    * 파일 디렉토리가 없을 시 자동 생성 및 디렉토리 체크
    */
    @PostConstruct
    public void init() {
        try {
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);
        } catch (IOException ex) {
            throw new RuntimeException("Could not create the upload directory: " + uploadDir, ex);
        }
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 파일 접근을 위한 리소스 핸들러 추가 (선택적)
        // 웹에서 업로드된 파일을 직접 접근할 수 있도록 설정
        // 주의: 보안에 민감한 파일은 이 방식으로 접근하지 않도록 주의
        try {
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            String uploadUri = uploadPath.toUri().toURL().toString();

            registry.addResourceHandler("/uploads/**")
                    .addResourceLocations(uploadUri);

        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while configuring resource handler", ex);
        }
    }
}
