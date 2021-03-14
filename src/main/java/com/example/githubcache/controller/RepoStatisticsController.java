package com.example.githubcache.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for retrieving data on GitHub repos for a
 * configured organization
 */
// NOTE: I chose to include /top/N in the base path of this controller as well, but I could see the argument that
// this is not a good idea if the controller will be more diverse than what's required for this project
@RequestMapping("/view/top/{N}")
@RestController
public class RepoStatisticsController {

    @GetMapping("/forks")
    public ResponseEntity<?> getTopForks(@PathVariable("N") Integer topN) {
        return null;
    }

    @GetMapping("/last_updated")
    public ResponseEntity<?> getTopLastUpdated(@PathVariable("N") Integer topN) {
        return null;
    }

    @GetMapping("/open_issues")
    public ResponseEntity<?> getTopOpenIssues(@PathVariable("N") Integer topN) {
        return null;
    }

    @GetMapping("/stars")
    public ResponseEntity<?> getTopStars(@PathVariable("N") Integer topN) {
        return null;
    }

    @GetMapping("/watchers")
    public ResponseEntity<?> getTopWatchers(@PathVariable("N") Integer topN) {
        return null;
    }
}
