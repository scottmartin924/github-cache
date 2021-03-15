package com.example.githubcache.representation;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
public class HomeRepresentation {
    private String currentUserUrl;
    private String currentUserAuthorizationsHtmlUrl;
    private String authorizationsUrl;
    private String codeSearchUrl;
    private String commitSearchUrl;
    private String emailsUrl;
    private String emojisUrl;
    private String eventsUrl;
    private String feedsUrl;
    private String followersUrl;
    private String followingUrl;
    private String gistsUrl;
    private String hubUrl;
    private String issueSearchUrl;
    private String issuesUrl;
    private String keysUrl;
    private String labelSearchUrl;
    private String notificationsUrl;
    private String organizationUrl;
    private String organizationRepositoriesUrl;
    private String organizationTeamsUrl;
    private String publicGistsUrl;
    private String rateLimitUrl;
    private String repositoryUrl;
    private String repositorySearchUrl;
    private String currentUserRepositoriesUrl;
    private String starredUrl;
    private String starredGistsUrl;
    private String userUrl;
    private String userOrganizationsUrl;
    private String userRepositoriesUrl;
    private String userSearchUrl;
}
