package com.example.githubcache.cache;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;

@AllArgsConstructor
@Data
public class CacheMapperException extends IOException {
    private String message;
    private Throwable root;
}
