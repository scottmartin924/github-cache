package com.example.githubcache.service;

import com.example.githubcache.cache.ResponseCache;
import com.example.githubcache.client.GithubClient;
import com.example.githubcache.config.Endpoints;
import com.example.githubcache.representation.RepoRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RepoStatisticsServiceImpl implements RepoStatisticsService {
    private final ResponseCache cache;
    private final GithubClient updateTasks;

    public RepoStatisticsServiceImpl(@Autowired ResponseCache cache,
                                     @Autowired GithubClient tasks) {
        this.cache = cache;
        this.updateTasks = tasks;
    }

    @Override
    public List<RepoRepresentation> findMostForked(int n) {
        return findTopN(n, Comparator.comparingInt(RepoRepresentation::getForksCount));
    }

    @Override
    public List<RepoRepresentation> findTopLastUpdated(int n) {
        // NOTE: It's not clear to me if this should be updated_at or pushed_at, we'll do updated_at for now, but
        // to me pushed_at seems more useful
        return findTopN(n, Comparator.comparing(RepoRepresentation::getUpdatedAt));
    }

    @Override
    public List<RepoRepresentation> findMostIssues(int n) {
        return findTopN(n, Comparator.comparing(RepoRepresentation::getOpenIssuesCount));
    }

    @Override
    public List<RepoRepresentation> findMostStarred(int n) {
        return findTopN(n, Comparator.comparing(RepoRepresentation::getStargazersCount));
    }

    // FIXME This number is wrong...no longer comes back from this api
    @Override
    public List<RepoRepresentation> findMostWatched(int n) {
        return findTopN(n, Comparator.comparing(RepoRepresentation::getWatchers));
    }

    /**
     * Find top n entries of list
     * @param n the number of entries to return
     * @param comparator the comparison to sort
     * @return top n values based on comparator
     */
    private List<RepoRepresentation> findTopN(int n, Comparator<RepoRepresentation> comparator) {
        return getRepos().stream()
                .sorted(comparator)
                .limit(n)
                .collect(Collectors.toList());
    }

    /**
     * Get repos from cache
     * @return
     */
    private List<RepoRepresentation> getRepos() {
        Optional<RepoRepresentation[]> representations = getReposFromCache();
        // FIXME Terrible use of optional here (maybe not have it be optional if this is the type of shit I'm using it for)
        // FIXME Refactor...this is rough
        if (representations.isEmpty()) {
            this.updateTasks.updateReposRoute();
            // Cache should be updated since repos just pull
            representations = getReposFromCache();
        }
        return Arrays.asList(representations.get());
    }

    private Optional<RepoRepresentation[]> getReposFromCache() {
        return cache.retrieveElement(Endpoints.REPOS.getKey(), RepoRepresentation[].class);
    }
}
