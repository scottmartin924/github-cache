package com.example.githubcache.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application.redis")
@Data
public class RedisConfiguration {
    private String host;
    private String port;
}
