package com.example.githubcache.config.cache;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("application.cache")
@Data
public class CacheConfiguration {
    private int evictionTime;
    private RedisConfiguration redis = new RedisConfiguration();

    @Data
    public final class RedisConfiguration {
        private String host;
        private String port;
    }
}
