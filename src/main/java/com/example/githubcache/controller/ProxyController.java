package com.example.githubcache.controller;

import com.example.githubcache.client.WebApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

// NOTE: It looks like this will catch everything, but spring resolves exact matches first so
// the views controller will take precedence over this controller when views are called
@RequestMapping("/")
@RestController
public class ProxyController {
    private static final Logger LOG = LoggerFactory.getLogger(RepoViewController.class);
    private final WebApiClient cachedClient;

    public ProxyController(@Autowired WebApiClient cachedClient) {
        this.cachedClient = cachedClient;
    }

    /**
     * Handle all non-handled GET requests. Note that the service WILL NOT
     * proxy non-GET endpoints at the moment
     *
     * @param request the servlet request
     * @return entity from either cache or proxied to github (which is then cached)
     */
    @GetMapping(value = "**")
    public ResponseEntity<?> proxyRequests(HttpServletRequest request) {
        LOG.info("Pulling data from cache or from remote. Request uri: {}", request.getRequestURI());
        final String baseUri = request.getRequestURI();
        final StringBuilder parameters = new StringBuilder();
        request.getParameterMap()
                .forEach((k, v) -> {
                    parameters.append("?").append(k).append("=").append(v[0]);
                });
        final String fullPath = baseUri + parameters.toString();
        return ResponseEntity.ok(cachedClient.getResource(fullPath, JsonNode.class));
    }
}
