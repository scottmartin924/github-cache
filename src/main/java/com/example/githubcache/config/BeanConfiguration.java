package com.example.githubcache.config;

import com.example.githubcache.cache.LocalCache;
import com.example.githubcache.cache.ResponseCache;
import com.example.githubcache.client.CacheAwareWebClient;
import com.example.githubcache.client.PaginationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
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
     * Configure response cache
     * @param configuration the app configuration needed to get configured eviction time
     * @return cache
     */
    @Bean
    public ResponseCache cache(@Autowired RemoteConfiguration configuration) {
        return new LocalCache(configuration.getEvictionTime());
    }

    /**
     * Construct cache aware webclient bean
     * @param configuration the app configuration to setup webclient
     * @param env the runtime environment to pull environment variables
     * @param cache the response cache to use with the client
     * @return a cache aware web client bean
     */
    @Bean
    public CacheAwareWebClient cacheAwareWebClient(@Autowired RemoteConfiguration configuration,
                                                   @Autowired Environment env,
                                                   @Autowired ResponseCache cache) {
        final String baseUrl = configuration.getBaseUrl();
        final RemoteConfiguration.AuthorizationConfig authorizationConfig = configuration.getAuthorization();
        Map<String, String> headers = new HashMap<>();
        if (authorizationConfig != null) {
            final String interpolatedAuthString = interpolateAuthorizationString(authorizationConfig.getValue(), env);
            headers.put(authorizationConfig.getHeader(), interpolatedAuthString);
        }
        final PaginationHandler paginationHandler = new PaginationHandler(configuration.getPaginationHeader());
        return new CacheAwareWebClient(cache, paginationHandler, baseUrl, headers);
    }

    @Bean
    public void test() {
        Jedis jd = new Jedis("localhost");
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
