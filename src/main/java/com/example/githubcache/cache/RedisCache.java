package com.example.githubcache.cache;

import redis.clients.jedis.Jedis;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RedisCache implements ResponseCache {
    // FIXME make cache object and class and have a tomap function on it??? Or....honestly idk
    private static final String VALUE_FIELD = "value";
    private static final String EVICTION_FIELD = "evictionTime";
    private static final String LAST_UPDATED_FIELD = "lastUpdated";

    private final Jedis jedis;
    private final Duration evictionDuration;
    private final CacheMapper mapper;


    public RedisCache(Jedis jedis, int evictionTime, CacheMapper mapper) {
        this.jedis = jedis;
        this.mapper = mapper;
        this.evictionDuration = Duration.ofMinutes(evictionTime);
    }

    @Override
    public CacheMapper getMapper() {
        return this.mapper;
    }

    @Override
    public void addElement(String key, Object object) {
        String value = mapper.toCacheObject(object);
        Instant now = Instant.now();
        HashMap<String, String> map = new HashMap<>();
        map.put(VALUE_FIELD, value);
        map.put(EVICTION_FIELD, mapper.toCacheObject(now.plus(evictionDuration)));
        map.put(LAST_UPDATED_FIELD, mapper.toCacheObject(now));
        jedis.hset(key, map);
    }

    @Override
    public <S> Optional<S> retrieveElement(String key, Class<S> type) {
        Map<String, String> cachedObject = jedis.hgetAll(key);
        if(cachedObject == null || cachedObject.isEmpty()) {
            return Optional.empty();
        }
        final String evictionTimeStr = cachedObject.get(EVICTION_FIELD);
        final Instant evictionTime = mapper.fromCacheObject(evictionTimeStr, Instant.class);
        if(Instant.now().isAfter(evictionTime)) {
            return Optional.empty();
        } else {
            final S transformedObj = mapper.fromCacheObject(cachedObject.get(VALUE_FIELD), type);
            return Optional.of(transformedObj);
        }
    }
}
