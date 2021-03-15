package com.example.githubcache.representation;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Date;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
public class OrgRepresentation {
    private String login;
    private int id;
    private String nodeId;
    private String url;
    private String reposUrl;
    private String eventsUrl;
    private String hooksUrl;
    private String issuesUrl;
    private String membersUrl;
    private String privateMembersIrl;
    private String avatarUrl;
    private String description;
    private String name;
    private Object company;
    private String blog;
    private String location;
    private Object email;
    private String twitterUsername;
    private boolean isVerified;
    private boolean hasOrganizationProjects;
    private boolean hasRepositoryProjects;
    private int privateRepos;
    private int privateGists;
    private int followers;
    private int following;
    private String htmlUrl;
    private Date createdAt;
    private Date updatedAt;
    private String type;
}
