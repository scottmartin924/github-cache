package com.example.githubcache.config;

import com.example.githubcache.cache.ResponseCache;
import com.example.githubcache.client.CacheAwareWebClient;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;

@Component
public class CacheUpdateScheduler {
    private final CacheAwareWebClient client;

    // FIXME This maybe shouldn't need a cache...CacheAwareWebClient could handle?
    private final ResponseCache cache;

    private final TaskScheduler scheduler;
    private final RemoteConfiguration configuration;

    public CacheUpdateScheduler(@Autowired CacheAwareWebClient client,
                                @Autowired TaskScheduler scheduler,
                                @Autowired RemoteConfiguration configuration,
                                @Autowired ResponseCache cache) {
        this.client = client;
        this.scheduler = scheduler;
        this.configuration = configuration;
        this.cache = cache;
    }

    /**
     * Create schedule to update cache
     */
    @PostConstruct
    private void createCacheUpdateSchedule() {
        final Duration scheduleRate = Duration.ofMinutes(configuration.getRefreshRate());
        scheduler.scheduleWithFixedDelay(this::updateCache, scheduleRate);
    }

    /**
     * Update all configured cached endpoints
     */
    private void updateCache() {
        this.configuration.getCachedEndpoints()
                .entrySet()
                .forEach(entry -> updateCacheForRemote(entry.getKey(), entry.getValue()));
    }

    /**
     * Update cache for given remote
     * @param name the name of the remote
     * @param endpoint the endpoint for the remote
     */
    private void updateCacheForRemote(String name, String endpoint) {
        ResponseEntity<JsonNode> entity = this.client.retrieveRemoteResource(endpoint);
        this.cache.addElement(name, entity.getBody());
    }
}
