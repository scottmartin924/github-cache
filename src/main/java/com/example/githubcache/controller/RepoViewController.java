package com.example.githubcache.controller;

import com.example.githubcache.service.RepoStatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// NOTE: I chose to include /top/N in the base path of this controller as well, but I could see the argument that
// this is not a good idea if the controller will be more diverse than what's required for this project

/**
 * Controller for retrieving data on GitHub repos for a
 * configured organization
 */
@RequestMapping("/view/top/{N}")
@RestController
public class RepoViewController {
    private static final Logger LOG = LoggerFactory.getLogger(RepoViewController.class);
    private final RepoStatisticsService service;

    public RepoViewController(@Autowired RepoStatisticsService service) {
        this.service = service;
    }

    // NOTE: Choosing not to include HATEOAS links because I suspect the consumer will be expecting a response
    // like what the GitHub api would send back and adding links would alter that (or that's my reasoning)

    @GetMapping("/forks")
    public ResponseEntity<?> getTopForks(@PathVariable("N") Integer topN) {
        LOG.debug("Get top forks. N={}", topN);
        return ResponseEntity.ok(this.service.findMostForked(topN));
    }

    @GetMapping("/last_updated")
    public ResponseEntity<?> getTopLastUpdated(@PathVariable("N") Integer topN) {
        LOG.debug("Get last updated. N={}", topN);
        return ResponseEntity.ok(this.service.findTopLastUpdated(topN));
    }

    @GetMapping("/open_issues")
    public ResponseEntity<?> getTopOpenIssues(@PathVariable("N") Integer topN) {
        LOG.debug("Get top open issues. N={}", topN);
        return ResponseEntity.ok(this.service.findMostIssues(topN));
    }

    @GetMapping("/stars")
    public ResponseEntity<?> getTopStars(@PathVariable("N") Integer topN) {
        LOG.debug("Get top stars. N={}", topN);
        return ResponseEntity.ok(this.service.findMostStarred(topN));
    }

    @GetMapping("/watchers")
    public ResponseEntity<?> getTopWatchers(@PathVariable("N") Integer topN) {
        LOG.debug("Get top watchers. N={}", topN);
        return ResponseEntity.ok(this.service.findMostForked(topN));
    }
}
