package com.example.githubcache.cache;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class LocalCache implements ResponseCache<String> {
    private static final ConcurrentHashMap<String, CacheObject> CACHE = new ConcurrentHashMap<>();
    private final int evictionTime;
    private final CacheMapper<String> mapper;

    public LocalCache(int evictionTime, JacksonCacheMapper mapper) {
        this.evictionTime = evictionTime;
        this.mapper = mapper;
    }

    @Override
    public CacheMapper<String> getMapper() {
        return this.mapper;
    }

    @Override
    public void addElement(String key, Object object) {
        String cacheValue = getMapper().toCacheObject(object);
        CacheObject cacheObject = CacheObject.builder()
                .value(cacheValue)
                .lastUpdated(Instant.now())
                .status(CacheObjectStatus.UPDATED)
                .build();
        CACHE.put(key, cacheObject);
    }

    @Override
    public <T> Optional<T> retrieveElement(String key, Class<T> type) {
        // FIXME Shouldn't caclulate eviction time without knowing if null...not a huge deal though
        Instant evictionInstant = Instant.now().plus(this.evictionTime, ChronoUnit.MINUTES);
        CacheObject cachedObject = CACHE.get(key);
        if (cachedObject == null || cachedObject.getLastUpdated().isBefore(evictionInstant)) {
            return Optional.empty();
        }
        T value = getMapper().fromCacheObject(cachedObject.getValue(), type);
        return Optional.of(value);
    }



    public void printCache() {
        CACHE.entrySet().forEach(System.out::println);
    }
}
