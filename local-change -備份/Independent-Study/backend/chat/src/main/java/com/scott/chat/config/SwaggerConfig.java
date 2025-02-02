package com.scott.chat.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springdoc.core.models.GroupedOpenApi;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
            .info(new Info()
                .title("Social Media API")
                .version("1.0")
                .description("社群媒體平台 API 文檔"));
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
            .group("用戶管理")
            .pathsToMatch("/api/members/**")
            .build();
    }

    @Bean
    public GroupedOpenApi postApi() {
        return GroupedOpenApi.builder()
            .group("貼文管理")
            .pathsToMatch("/api/posts/**")
            .build();
    }

    @Bean
    public GroupedOpenApi messageApi() {
        return GroupedOpenApi.builder()
            .group("留言管理")
            .pathsToMatch("/api/messages/**")
            .build();
    }

    @Bean
    public GroupedOpenApi collectApi() {
        return GroupedOpenApi.builder()
            .group("收藏管理")
            .pathsToMatch("/api/collects/**")
            .build();
    }

    @Bean
    public GroupedOpenApi photoApi() {
        return GroupedOpenApi.builder()
            .group("圖片管理")
            .pathsToMatch("/api/post-photos/**")
            .build();
    }
    
    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
            .group("認證授權")
            .pathsToMatch("/api/auth/**", "/login/**", "/oauth2/**")
            .build();
    }
}