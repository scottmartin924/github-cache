package com.example.githubcache.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "application.remote")
@Data
public class RemoteConfiguration {
    private String githubApiToken;
    private String baseUrl;
    private int refreshRate;
    private int evictionTime;
    private String paginationHeader;
    private final RemoteConfiguration.AuthorizationConfig authorization = new RemoteConfiguration.AuthorizationConfig();
    private Map<String, String> cachedEndpoints = new HashMap<>();

    @Data
    public static class AuthorizationConfig {
        private String header;
        private String value;
    }
}
