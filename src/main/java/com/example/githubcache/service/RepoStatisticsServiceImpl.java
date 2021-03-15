package com.example.githubcache.service;

import com.example.githubcache.client.WebApiClient;
import com.example.githubcache.config.web.RouteBuilder;
import com.example.githubcache.representation.RepoRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RepoStatisticsServiceImpl implements RepoStatisticsService {
    private static final Logger LOG = LoggerFactory.getLogger(RepoStatisticsServiceImpl.class);

    private final RouteBuilder routes;
    private final WebApiClient client;

    public RepoStatisticsServiceImpl(@Autowired WebApiClient client, @Autowired RouteBuilder routes) {
        this.routes = routes;
        this.client = client;
    }

    @Override
    public List<RepoRepresentation> findMostForked(int n) {
        LOG.debug("Find top {} most forked repos.", n);
        return findTopN(n, Comparator.comparingInt(RepoRepresentation::getForksCount));
    }

    @Override
    public List<RepoRepresentation> findTopLastUpdated(int n) {
        // NOTE: It's not clear to me if this should be updated_at or pushed_at, we'll do updated_at for now, but
        // to me pushed_at seems more useful
        LOG.debug("Find top {} last updated repos.", n);
        return findTopN(n, Comparator.comparing(RepoRepresentation::getUpdatedAt));
    }

    @Override
    public List<RepoRepresentation> findMostIssues(int n) {
        LOG.debug("Find top {} repos with most open issues.", n);
        return findTopN(n, Comparator.comparing(RepoRepresentation::getOpenIssuesCount));
    }

    @Override
    public List<RepoRepresentation> findMostStarred(int n) {
        LOG.debug("Find top {} most starred repos.", n);
        return findTopN(n, Comparator.comparing(RepoRepresentation::getStargazersCount));
    }

    // NOTE: subscribers_count no longer comes back from this API. Watchers is a relic of an older
    // api which is now the same as stars. So this number is wrong, but the work required to get
    // subscribers_count seems pretty extreme
    @Override
    public List<RepoRepresentation> findMostWatched(int n) {
        LOG.debug("Find top {} most watched repos.", n);
        return findTopN(n, Comparator.comparing(RepoRepresentation::getWatchers));
    }

    // NOTE: reversing comparator in method saves some redundant code, but also seems a little counterintuitive

    /**
     * Find top n entries of list
     *
     * @param n          the number of entries to return
     * @param comparator the comparison to sort. NOTE: Since we want top N the given comparator will be reversed
     *                   in this method. That is, pass in the "regular" (ascending) comparator and the method
     *                   reverses is to get descending order
     * @return top n values based on comparator
     */
    private List<RepoRepresentation> findTopN(int n, Comparator<RepoRepresentation> comparator) {
        return getRepos().stream()
                .sorted(comparator.reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    /**
     * Get repos from client (either from cache or api)
     *
     * @return list of repo representations
     */
    private List<RepoRepresentation> getRepos() {
        return Arrays.asList(client.getResource(routes.route(RouteBuilder.CachedEndpoint.REPOS), RepoRepresentation[].class));
    }
}
