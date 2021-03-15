package com.example.githubcache.config.web;

/**
 * Build routes for enpoints we cache on a schedule
 */
public class RouteBuilder {

    private final String org;

    public RouteBuilder(String org) {
        this.org = org;
    }

    public String route(CachedEndpoint endpoint) {
        return String.format(endpoint.baseRoute, this.org);
    }

    /**
     * Enum of endpoints we cache on a schedule
     */
    public enum CachedEndpoint {
        HOME("/"),
        ORGS("/orgs/%s"),
        MEMBERS("/orgs/%s/members"),
        REPOS("/orgs/%s/repos");

        private final String baseRoute;

        CachedEndpoint(String base) {
            this.baseRoute = base;
        }
    }
}
