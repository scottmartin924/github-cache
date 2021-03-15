package com.example.githubcache.client;

import com.example.githubcache.cache.ResponseCache;
import com.example.githubcache.config.EndpointConfiguration;
import com.example.githubcache.config.EndpointTaskList;
import com.example.githubcache.config.Endpoints;
import com.example.githubcache.representation.MemberRepresentation;
import com.example.githubcache.representation.OrgRepresentation;
import com.example.githubcache.representation.RepoRepresentation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class GithubClient {
    private WebClient client;
    private ResponseCache cache;
    private EndpointTaskList taskList;
    private String organization;
    private PaginationHandler handler;

    public GithubClient(WebClient client, ResponseCache cache, String organization, String linkHeader) {
        this.client = client;
        this.cache = cache;
        this.organization = organization;
        this.handler = new PaginationHandler(linkHeader);
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
        this.taskList.executeTaskList(this.organization);
    }

    // FIXME Make nonblocking
    // FIXME Handle errors!!!
    public void updateHomeRoute() {
        updateEndpointSingleEntity(Endpoints.HOME, String.class);
    }

    public void updateOrgRoute() {
        updateEndpointSingleEntity(Endpoints.ORGS, OrgRepresentation.class);
    }

    public void updateMembersRoute() {
        updateEndpointList(Endpoints.MEMBERS, MemberRepresentation[].class);
    }

    public void updateReposRoute() {
        updateEndpointList(Endpoints.REPOS, RepoRepresentation[].class);
    }

    /**
     * Update endpoint value in cache
     * @param configuration the endpoint configuration
     * @param clazz the class to save in cache
     * @param <T> the type of the value to save in cache
     */
    private <T> void updateEndpointSingleEntity(final EndpointConfiguration configuration, Class<T> clazz) {
        List<T> completeList = new ArrayList<>();
        final String route = configuration.formatEndpoint(this.organization);
        final ResponseEntity<T> repoRepresentation = this.client
                .get()
                .uri(route)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(clazz)
                .block();

        cache.addElement(configuration.getKey(), repoRepresentation.getBody());
    }

    /**
     * Update endpoint value in cache
     * @param configuration the endpoint configuration
     * @param type the parameterizable type to save in cache
     * @param <T> the type of the value to save in cache
     */
    private <T> void updateEndpointList(final EndpointConfiguration configuration, Class<T[]> type) {
        List<T> completeList = new ArrayList<>();
        Optional<String> next = Optional.of(configuration.formatEndpoint(this.organization));
        do {
            final ResponseEntity<T[]> repoRepresentation = this.client
                    .get()
                    .uri(next.get())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(type)
                    .block();
            completeList.addAll(Arrays.asList(repoRepresentation.getBody()));
            next = handler.nextPageUrl(repoRepresentation.getHeaders());
        } while(next.isPresent());
        cache.addElement(configuration.getKey(), completeList);
    }
}
