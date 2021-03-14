package com.example.githubcache.service;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

/**
 * RepoStatisticsService base class
 */
public abstract class RepoStatisticsService {

    protected static final String ENDPOINT_NAME = "repos";

    // FIXME Not sure I like this...maybe put it in an enum or something...not my favorite
    private static final String FORKED_FIELD = "";
    private static final String LAST_UPDATED_FIELD = "";
    private static final String MOST_STARRED_FIELD = "";
    private static final String MOST_WATCHED_FIELD = "";
    private static final String MOST_ISSUES_FIELD = "";

    /**
     * Find top n values for a field for the available list of repos
     * @param field the fields to find the top n values for
     * @param n the number of repos to find
     * @return list of the top n repos for the field
     */
    public abstract List<JsonNode> findTopN(String field, int n);

    /**
     * Find most top n forked repositories
     * @param n number of repos to find
     * @return list of repos as json objects
     */
    public List<JsonNode> findMostForked(int n) {
        return findTopN(FORKED_FIELD, n);
    }

    /**
     * Find top n last updated
     * @param n number of repos
     * @return list of repos as json objects
     */
    public List<JsonNode> findTopLastUpdated(int n) {
        return findTopN(LAST_UPDATED_FIELD, n);
    }

    /**
     * Find top n repos with most issues
     * @param n number of repos
     * @return list of repos as json objects
     */
    public List<JsonNode> findMostIssues(int n) {
        return findTopN(MOST_ISSUES_FIELD, n);
    }

    /**
     * Find top n most starred repos
     * @param n number of repos
     * @return list of repos as json objects
     */
    public List<JsonNode> findMostStarred(int n) {
        return findTopN(MOST_STARRED_FIELD, n);
    }

    /**
     * Find top n most watched repos
     * @param n number of repos
     * @return list of repos as json objects
     */
    public List<JsonNode> findMostWatched(int n) {
        return findTopN(MOST_WATCHED_FIELD, n);
    }
}
