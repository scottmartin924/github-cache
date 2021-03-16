package com.example.githubcache.client;

import com.example.githubcache.config.web.EndpointTaskList;
import com.example.githubcache.config.web.RouteBuilder;
import com.example.githubcache.representation.HomeRepresentation;
import com.example.githubcache.representation.MemberRepresentation;
import com.example.githubcache.representation.OrgRepresentation;
import com.example.githubcache.representation.RepoRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class to update enpoints that are cached by default
 */
@Component
public class EndpointUpdateClient {
    private static final Logger LOG = LoggerFactory.getLogger(EndpointUpdateClient.class);

    private WebApiClient client;
    private EndpointTaskList taskList;
    private RouteBuilder routes;

    public EndpointUpdateClient(@Autowired WebApiClient client,
                                @Autowired RouteBuilder routes) {
        this.client = client;
        this.routes = routes;
        this.taskList = new EndpointTaskList.Builder()
                .addTask(this::updateHomeRoute)
                .addTask(this::updateOrgRoute)
                .addTask(this::updateMembersRoute)
                .addTask(this::updateReposRoute)
                .build();
    }

    /**
     * Execute the task lists to update caches
     */
    public void execute() {
        LOG.info("Executing scheduled task list.");
        this.taskList.executeTaskList();
    }

    /**
     * Update home github data if expired
     */
    public void updateHomeRoute() {
        // NOTE: use JsonNode to avoid making a representation for the home response which is basi
        client.getResource(routes.route(RouteBuilder.CachedEndpoint.HOME), HomeRepresentation.class);
    }

    /**
     * Update org data if expired
     */
    public void updateOrgRoute() {
        client.getResource(routes.route(RouteBuilder.CachedEndpoint.ORGS), OrgRepresentation.class);
    }

    /**
     * update members data if expired
     */
    public void updateMembersRoute() {
        client.getResource(routes.route(RouteBuilder.CachedEndpoint.MEMBERS), MemberRepresentation[].class);
    }

    /**
     * Update repos data if expired
     */
    public void updateReposRoute() {
        client.getResource(routes.route(RouteBuilder.CachedEndpoint.REPOS), RepoRepresentation[].class);
    }
}
