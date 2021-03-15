package com.example.githubcache.config.web;

import lombok.Getter;

@Getter
public class EndpointConfiguration {
    private final String key;
    private final String route;

    public EndpointConfiguration(String key, String route) {
        this.key = key;
        this.route = route;
    }

    /**
     * Get endpoint and format with org
     * @param org the org to format with
     * @return formatted endpoint
     */
    public String formatEndpoint(final String org) {
        return String.format(this.route, org);
    }
}