package com.example.githubcache.service;

import com.example.githubcache.cache.ResponseCache;
import com.example.githubcache.client.CacheAwareWebClient;
import com.example.githubcache.config.RemoteConfiguration;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepoStatisticsServiceImpl extends RepoStatisticsService {
    private final ResponseCache cache;
    private final String endpoint;
    private final CacheAwareWebClient client;

    // FIXME If we have to take in the entire configuration then build this as a bean not in component scan
    public RepoStatisticsServiceImpl(@Autowired ResponseCache cache,
                                     @Autowired CacheAwareWebClient client,
                                     @Autowired RemoteConfiguration remoteConfiguration) {
        this.cache = cache;
        this.client = client;
        final String endpoint = remoteConfiguration.getCachedEndpoints().get(RepoStatisticsService.ENDPOINT_NAME);
        if(endpoint == null) {
            throw new RuntimeException(String.format("View %s not found in the configuration.", RepoStatisticsService.ENDPOINT_NAME));
        }
        this.endpoint = endpoint;
    }

    @Override
    public List<JsonNode> findTopN(String field, int n) {
        JsonNode repos = cache.retrieveElement(ENDPOINT_NAME)
                                .orElseGet(() -> {
                                    ResponseEntity<JsonNode> entity = client.retrieveRemoteResource(endpoint);
                                    return entity.getBody();
                                });

        return null;
    }
}
