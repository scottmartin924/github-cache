package com.example.githubcache.controller;

import com.example.githubcache.cache.ResponseCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("proxy")
@RestController
public class ProxyController {

    @Autowired
    public ResponseCache cache;

    @GetMapping
    public ResponseEntity<?> printCache() {
        cache.printCache();
        return ResponseEntity.ok().build();
    }
}
