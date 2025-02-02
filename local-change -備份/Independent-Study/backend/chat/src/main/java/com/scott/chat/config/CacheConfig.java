package com.scott.chat.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import java.util.Arrays;

@Configuration
@EnableCaching
public class CacheConfig {
    
    // 主要的緩存管理器配置
    // 使用簡單的 ConcurrentMap 實現，適合開發環境和小型應用
    @Bean
    @Primary
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(
            new ConcurrentMapCache("posts"),
            new ConcurrentMapCache("comments"),
            new ConcurrentMapCache("users"),
            new ConcurrentMapCache("sessions"),
            new ConcurrentMapCache("likes"),
            new ConcurrentMapCache("notifications")
        ));
        return cacheManager;
    }
}