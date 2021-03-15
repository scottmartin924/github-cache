package com.example.githubcache.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RequestMapping("/")
@RestController
public class ProxyController {
    private final WebClient client;

    public ProxyController(@Autowired WebClient client) {
        this.client = client;
    }

    // FIXME Implement pull from cache for the rest of endpoints...need to figure that out

    // FIXME Add rest of these...need to add way to get rest of path
    @GetMapping
    public ResponseEntity<?> printCache() {
        return ResponseEntity.ok().build();
    }


}
