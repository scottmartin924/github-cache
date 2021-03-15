package com.example.githubcache.client;

import com.example.githubcache.cache.ResponseCache;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.Optional;

/**
 * Web client which will either retrieve element
 * from remote location via http request or from
 * cache if populated in cache
 */
public class CacheAwareWebClient {

    // FIXME Use this to cache other proxied requests

//    // Really the webclient should also be parameterized, but oh well
//    private final ResponseCache<String> cache;
//    private final WebClient client;
//    private final PaginationHandler handler;
//
//    // FIXME Should I include more options for building the webclient? Probably...maybe include a builder?
//    public CacheAwareWebClient(ResponseCache<String> cache,
//                               PaginationHandler handler,
//                               String baseUrl,
//                               Map<String, String> defaultHeaders) {
//        this.cache = cache;
//        this.handler = handler;
//        this.client = WebClient.builder()
//                        .baseUrl(baseUrl)
//                        .defaultHeaders(headers -> {
//                            defaultHeaders.forEach((key, value) -> headers.set(key, value));
//                        })
//                        .build();
//    }
//
//    /**
//     * Get resource either from cache or remote location
//     * @param key cache key
//     * @param location
//     * @return
//     */
//    public <T> T getResource(String key, String location, Class<T> resourceType) {
//        // FIXME Maybe this de/serialization layer should be by itself...doesn't belong here
//
//        cache.retrieveElement(location, resourceType);
//        // Check if resource in cache and unexpired...if it is then return it, else update the value
//        T resource = cache.retrieveElement(location, resourceType)
//                .orElseGet(() -> {
//                    ResponseEntity<T> response = retrieveRemoteResource(location, resourceType);
//                    return response.getBody();
//                });
//        cache.addElement(location, resource);
//        return resource;
//    }
//
//    /**
//     * Retreive remote resources as JsonNode
//     * @param remoteLocation the location
//     * @return response entity with jsonnode body
//     */
//    public ResponseEntity<JsonNode> retrieveRemoteResource(String remoteLocation) {
//        return retrieveRemoteResource(remoteLocation, JsonNode.class);
//    }
//
//    /**
//     * Retrieve resource from remote location
//     * @param remoteLocation the location
//     * @return response entity with json node
//     */
//    public <T> ResponseEntity<T> retrieveRemoteResource(String remoteLocation, Class<T> returnType) {
//        // FIXME Handle error if unable to retrieve
//        // FIXME This shouldn't block...not sure how to handle it at the moment without turning the entire thing to be non-blocking (which is an okay option)
//        // FIXME Currently only handles json objects b/c we convert to a jsonnode...I think for now that's okay
//        return this.client.get()
//                .uri(remoteLocation)
//                .accept(MediaType.APPLICATION_JSON)
//                .retrieve()
//                .toEntity(returnType)
//                .block();
//    }
//
//    /**
//     * Retrieve resource from remote location
//     * @param remoteLocation the location
//     * @return response entity with json node
//     */
//    public <T> ResponseEntity<T> retrieveRemoteResource(String remoteLocation, ParameterizedTypeReference<T> returnType) {
//        // FIXME Handle error if unable to retrieve
//        // FIXME This shouldn't block...not sure how to handle it at the moment without turning the entire thing to be non-blocking (which is an okay option)
//        // FIXME Currently only handles json objects b/c we convert to a jsonnode...I think for now that's okay
//        return this.client.get()
//                .uri(remoteLocation)
//                .accept(MediaType.APPLICATION_JSON)
//                .retrieve()
//                .toEntity(returnType)
//                .block();
//    }

    
}
