package com.example.githubcache.config.web;

import com.example.githubcache.client.PaginationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
public class WebRemoteConfiguration {
    private static final String API_TOKEN_PROP_NAME = "GITHUB-API-TOKEN";
    final Pattern VARIABLE_ITERP_PATTERN = Pattern.compile(".*(\\{.*?\\})");

    /**
     * Create pagination handler using header for pagination
     *
     * @param configuration config containing pagination header name
     * @return pagination handler
     */
    @Bean
    public PaginationHandler paginationHandler(@Autowired RemoteConfigurationProperties configuration) {
        return new PaginationHandler(configuration.getPaginationHeader());
    }

    /**
     * Create custom webclient using app configuration
     *
     * @param configuration the remote app configuration
     * @param env           the system environment to pull github_api_token from
     * @return configured webclient
     */
    @Bean
    public WebClient webClient(@Autowired RemoteConfigurationProperties configuration,
                               @Autowired Environment env) {
        final String baseUrl = configuration.getBaseUrl();
        final RemoteConfigurationProperties.AuthorizationConfig authorizationConfig = configuration.getAuthorization();
        Map<String, String> headers = new HashMap<>();
        if (authorizationConfig != null) {
            final String interpolatedAuthString = interpolateAuthorizationString(authorizationConfig.getValue(), env);
            headers.put(authorizationConfig.getHeader(), interpolatedAuthString);
        }
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeaders(hd -> {
                    headers.forEach((key, value) -> hd.set(key, value));
                })
                .build();
    }

    /**
     * Construct route builder
     *
     * @param configuration the configuration properties of the app to find organization
     * @return configured route builder
     */
    @Bean
    public RouteBuilder routes(@Autowired RemoteConfigurationProperties configuration) {
        return new RouteBuilder(configuration.getOrganization());
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
