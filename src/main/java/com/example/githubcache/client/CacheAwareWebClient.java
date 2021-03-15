package com.example.githubcache.client;

import com.example.githubcache.cache.ResponseCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Web client which will either retrieve element
 * from remote location via http request or from
 * cache if populated in cache
 */
@Component
public class CacheAwareWebClient implements WebApiClient {
    private static final Logger LOG = LoggerFactory.getLogger(CacheAwareWebClient.class);

    private final ResponseCache cache;
    private final WebClient client;
    private final PaginationHandler pagination;

    public CacheAwareWebClient(@Autowired ResponseCache cache,
                               @Autowired WebClient client,
                               @Autowired PaginationHandler pagination) {
        this.cache = cache;
        this.client = client;
        this.pagination = pagination;
    }

    /**
     * Get resource either from cache or making remote call
     *
     * @param location   the location of the resource
     * @param returnType the return type
     * @param <T>
     * @return the resource
     */
    @Override
    public <T> T getResource(String location, Class<T> returnType) {
        LOG.debug("Requested remote resource from {}", location);
        if (dataNeedsUpdated(location)) {
            LOG.debug("Data not present in cache. Pulling from remote resource {}", location);
            if (returnType.isArray()) {
                updateEndpointList(location, (Class<T[]>) returnType);
            } else {
                updateEndpoint(location, returnType);
            }
        }
        // NOTE: This whole project really, but this spot in particular is a great example of how not to use optionals
        // NOTE 2: This whole flow depends on being synchronous...this optional is only non-empty if the update
        // above already completed. Not good, should refactor to avoid this
        return cache.retrieveElement(location, returnType).get();
    }


    /**
     * Check if data in cache is expired
     *
     * @param key the key of the data to determine if expired
     * @return true if data is expired and needs updated, else false
     */
    private boolean dataNeedsUpdated(final String key) {
        return cache.isExpired(key);
    }

    /**
     * Update endpoint value in cache
     *
     * @param location the endpoint location
     * @param clazz    the class to save in cache
     * @param <T>      the type of the value to save in cache
     */
    private <T> void updateEndpoint(final String location, Class<T> clazz) {
        // NOTE: Would really prefer not to block here, but another piece of code depends on it...awful
        ResponseEntity<T> response = this.client
                .get()
                .uri(location)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(clazz)
                .block();

        cache.addElement(location, response.getBody());
    }

    /**
     * Update endpoint value in cache
     *
     * @param location the endpoint location
     * @param type     the parameterizable type to save in cache
     * @param <T>      the type of the value to save in cache
     */
    private <T> void updateEndpointList(final String location, Class<T[]> type) {
        List<T> completeList = new ArrayList<>();
        String next = location;
        do {
            // NOTE: Not a fan of blocking here, but it does legitimately require a response to continue
            final ResponseEntity<T[]> repoRepresentation = getDataFromRemote(next, type).block();
            completeList.addAll(Arrays.asList(repoRepresentation.getBody()));
            next = pagination.nextPageUrl(repoRepresentation.getHeaders());
        } while (next != null);
        cache.addElement(location, completeList);
    }

    /**
     * Get data from remote location
     *
     * @param location   the location to get data from
     * @param returnType the type of the body to return
     * @param <T>        the retuern type of the body
     * @return response entity with body of type T
     */
    private <T> Mono<ResponseEntity<T>> getDataFromRemote(final String location, Class<T> returnType) {
        return this.client
                .get()
                .uri(location)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(returnType);
    }
}
