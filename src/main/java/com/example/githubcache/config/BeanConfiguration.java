package com.example.githubcache.config;

import com.example.githubcache.cache.JacksonCacheMapper;
import com.example.githubcache.cache.RedisCache;
import com.example.githubcache.cache.ResponseCache;
import com.example.githubcache.client.GithubClient;
import com.example.githubcache.client.PaginationHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.reactive.function.client.WebClient;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
public class BeanConfiguration {
    private static final String API_TOKEN_PROP_NAME = "GITHUB-API-TOKEN";
    final Pattern VARIABLE_ITERP_PATTERN = Pattern.compile(".*(\\{.*?\\})");

    /**
     * Create jackson object mapper
     * @return a configured object mapper
     */
    @Bean
    public ObjectMapper mapper() {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
        return mapper;
    }

    @Bean
    public ResponseCache cache(@Autowired RedisConfiguration configuration,
                               @Autowired ApplicationConfiguration applicationConfiguration,
                               @Autowired ObjectMapper mapper) {
        Jedis jedis = new Jedis(configuration.getHost());
        JacksonCacheMapper jacksonCacheMapper = new JacksonCacheMapper(mapper);
        return new RedisCache(jedis, applicationConfiguration.getEvictionTime(), jacksonCacheMapper);
    }

    @Bean
    public WebClient cacheAwareWebClient(@Autowired ApplicationConfiguration configuration,
                                         @Autowired Environment env) {
        final String baseUrl = configuration.getBaseUrl();
        final ApplicationConfiguration.AuthorizationConfig authorizationConfig = configuration.getAuthorization();
        Map<String, String> headers = new HashMap<>();
        if (authorizationConfig != null) {
            final String interpolatedAuthString = interpolateAuthorizationString(authorizationConfig.getValue(), env);
            headers.put(authorizationConfig.getHeader(), interpolatedAuthString);
        }
        final PaginationHandler paginationHandler = new PaginationHandler(configuration.getPaginationHeader());
        return WebClient.builder()
                        .baseUrl(baseUrl)
                        .defaultHeaders(hd -> {
                            headers.forEach((key, value) -> hd.set(key, value));
                        })
                        .build();
    }


    @Bean
    public GithubClient cacheUpdateTasks(@Autowired WebClient client,
                                         @Autowired ResponseCache cache,
                                         @Autowired ApplicationConfiguration configuration) {
        return new GithubClient(client, cache, configuration.getOrganization(), configuration.getPaginationHeader());
    }

    /**
     * Interpolate authorization string replacing placeholders for environment variables
     * @param authorizationString the raw authorization string with environment variable placeholders
     * @param env the runtime environment
     * @return interpolated authorization string
     */
    private String interpolateAuthorizationString(String authorizationString, Environment env) {
        final String token = env.getProperty(API_TOKEN_PROP_NAME);
        if(token == null || token.isEmpty()) {
            // FIXME Logging
            throw new RuntimeException(String.format("No API Token found....please specify a GitHub API token in the environment variable %s", API_TOKEN_PROP_NAME));
        }
        final Matcher matcher = VARIABLE_ITERP_PATTERN.matcher(authorizationString);
        // If authorizationString has values to replace then pull from environment variables if not then return as is
        if (matcher.matches()) {
            // Get environment variable name in form {ENVIRONMENT_VARIABLE}
            final String variableMatch = matcher.group(1);
            final String variableName = variableMatch.substring(1, variableMatch.length()-1);
            final String envVariable = env.getRequiredProperty(variableName);
            return authorizationString.replace(variableMatch, envVariable);
        } else {
            return authorizationString;
        }
    }
}
