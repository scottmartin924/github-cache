package com.example.githubcache.service;

import com.example.githubcache.representation.RepoRepresentation;

import java.util.List;

/**
 * RepoStatisticsService base class
 */
public interface RepoStatisticsService {

    // FIXME If can refactor should I make all these default methods??

    /**
     * Find most top n forked repositories
     * @param n number of repos to find
     * @return list of repos as json objects
     */
    List<RepoRepresentation> findMostForked(int n);

    /**
     * Find top n last updated
     * @param n number of repos
     * @return list of repos as json objects
     */
    List<RepoRepresentation> findTopLastUpdated(int n);

    /**
     * Find top n repos with most issues
     * @param n number of repos
     * @return list of repos as json objects
     */
    List<RepoRepresentation> findMostIssues(int n);

    /**
     * Find top n most starred repos
     * @param n number of repos
     * @return list of repos as json objects
     */
    List<RepoRepresentation> findMostStarred(int n);

    /**
     * Find top n most watched repos
     * @param n number of repos
     * @return list of repos as json objects
     */
    List<RepoRepresentation> findMostWatched(int n);
}
