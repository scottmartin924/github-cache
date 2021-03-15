package com.example.githubcache.config.web;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "application.remote")
@Data
public class RemoteConfigurationProperties {
    private String githubApiToken;
    private String baseUrl;
    private int evictionTime;
    private String paginationHeader;
    private String organization;
    private final RemoteConfigurationProperties.AuthorizationConfig authorization = new RemoteConfigurationProperties.AuthorizationConfig();
    private Map<String, String> cachedEndpoints = new HashMap<>();

    @Data
    public static class AuthorizationConfig {
        private String header;
        private String value;
    }
}
