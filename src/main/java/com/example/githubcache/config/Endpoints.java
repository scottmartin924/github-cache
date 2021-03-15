package com.example.githubcache.config;

/**
 * Configured endpoints to refresh and keep cached
 */
public class Endpoints {
    // FIXME I don't care for this, but I've got no better ideas at the moment
    public static final EndpointConfiguration HOME = new EndpointConfiguration("home", "/");
    public static final EndpointConfiguration ORGS = new EndpointConfiguration("orgs", "/orgs/%s");
    public static final EndpointConfiguration MEMBERS = new EndpointConfiguration("members", "/orgs/%s/members");
    public static final EndpointConfiguration REPOS = new EndpointConfiguration("repos", "/orgs/%s/repos");

}
