package com.example.githubcache.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/healthcheck")
@RestController
public class HealthController {

    /**
     * Health check. If service is reachable returns
     * a 200
     *
     * @return 200 response
     */
    @GetMapping
    public ResponseEntity<?> healthcheck() {
        return ResponseEntity.ok().build();
    }

    /**
     * Detailed health check showing the last time each configured
     * github api was retrieved and the number of failures or if
     * any github api is in a circuit breaker open position
     *
     * @return 200 with information from
     */
    @GetMapping("/detailed")
    public ResponseEntity<?> detailedHealthCheck() {
        return null;
    }
}
