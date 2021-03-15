package com.example.githubcache.controller;

import com.example.githubcache.cache.ResponseCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/healthcheck")
@RestController
public class HealthController {
    private static final Logger LOG = LoggerFactory.getLogger(HealthController.class);

    // Include cache in here so can check cache is connected and healthy (this is sort of a lazy approach to it)
    private final ResponseCache cache;

    public HealthController(@Autowired ResponseCache cache) {
        this.cache = cache;
    }

    /**
     * Health check. If service is reachable returns
     * a 200
     *
     * @return 200 response
     */
    @GetMapping
    public ResponseEntity<?> healthcheck() {
        LOG.debug("Health check endpoint hit.");
        HttpStatus status;
        StringBuilder response = new StringBuilder("Web service healthy. ");
        if (cache.isConnected()) {
            status = HttpStatus.OK;
            response.append("Cache connection healthy.");
        } else {
            // I love that this is an http status and finally get to use it
            status = HttpStatus.I_AM_A_TEAPOT;
            response.append("No cache connection...uh oh");
        }
        return ResponseEntity.status(status).body(response);
    }
}
