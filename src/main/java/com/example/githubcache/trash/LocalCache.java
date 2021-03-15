package com.example.githubcache.trash;

import com.example.githubcache.cache.CacheMapper;
import com.example.githubcache.cache.CacheObject;
import com.example.githubcache.cache.JacksonCacheMapper;
import com.example.githubcache.cache.ResponseCache;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class LocalCache implements ResponseCache {
    private static final ConcurrentHashMap<String, CacheObject> CACHE = new ConcurrentHashMap<>();
    private final int evictionTime;
    private final Duration evictionDuration;
    private final CacheMapper mapper;

    public LocalCache(int evictionTime, JacksonCacheMapper mapper) {
        this.evictionTime = evictionTime;
        this.evictionDuration = Duration.ofMinutes(evictionTime);
        this.mapper = mapper;
    }

    @Override
    public CacheMapper getMapper() {
        return this.mapper;
    }

    @Override
    public void addElement(String key, Object object) {
        String cacheValue = getMapper().toCacheObject(object);
        Instant now = Instant.now();
        CacheObject cacheObject = CacheObject.builder()
                .value(cacheValue)
                .lastUpdated(now)
                .evictionTime(now.plus(evictionDuration))
                .build();
        CACHE.put(key, cacheObject);
    }

    @Override
    public <T> Optional<T> retrieveElement(String key, Class<T> type) {
        CacheObject cachedObject = CACHE.get(key);
        if (cachedObject == null || cachedObject.getEvictionTime().isBefore(Instant.now())) {
            return Optional.empty();
        }
        T value = getMapper().fromCacheObject(cachedObject.getValue(), type);
        return Optional.of(value);
    }



    public void printCache() {
        CACHE.entrySet().forEach(System.out::println);
    }
}
