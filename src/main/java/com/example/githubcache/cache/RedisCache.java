package com.example.githubcache.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class RedisCache implements ResponseCache {
    private static final Logger LOG = LoggerFactory.getLogger(JacksonCacheMapper.class);

    private static final String VALUE_FIELD = "value";
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
        LOG.debug("Adding element key {} to cache.", key);
        try {
            String value = mapper.toCacheObject(object);
            Instant now = Instant.now();
            HashMap<String, String> map = new HashMap<>();
            map.put(VALUE_FIELD, value);
            map.put(LAST_UPDATED_FIELD, mapper.toCacheObject(now));
            jedis.hset(key, map);
        } catch (CacheMapperException exc) {
            // Taking no action but logging isn't my favorite here, but hard to know what else to do
            LOG.error("Unable to parse element to add to cache.", exc);
        }
    }

    @Override
    public <S> Optional<S> retrieveElement(String key, Class<S> type) {
        LOG.debug("Retrieving element key {} from cache", key);
        Map<String, String> cachedObject = getData(key);
        if (cachedObject == null || cachedObject.isEmpty()) {
            return Optional.empty();
        } else {
            try {
                final S transformedObj = mapper.fromCacheObject(cachedObject.get(VALUE_FIELD), type);
                return Optional.of(transformedObj);
            } catch (CacheMapperException exc) {
                // Taking no action but logging isn't my favorite here, but hard to know what else to do
                LOG.error("Unable to parse element from cache. Cache might be corrupted.", exc);
                return Optional.empty();
            }
        }
    }

    @Override
    public boolean isExpired(String key) {
        Map<String, String> cachedObject = getData(key);
        return cachedObject == null;
    }

    @Override
    public boolean isConnected() {
        Set<String> keys = this.jedis.keys("*");
        return keys != null;
    }

    /**
     * Get data if not expired
     *
     * @param key the key of the data
     * @return empty optional if expired else raw data
     */
    private Map<String, String> getData(String key) {
        Map<String, String> cachedObject = jedis.hgetAll(key);
        if (cachedObject == null || cachedObject.isEmpty()) {
            return null;
        }
        final String lastUpdatedStr = cachedObject.get(LAST_UPDATED_FIELD);
        final Instant lastUpdated;
        try {
            lastUpdated = mapper.fromCacheObject(lastUpdatedStr, Instant.class);
        } catch (CacheMapperException exc) {
            LOG.error("Unable to parse last updated string from cache. Cache might be corrupted.", exc);
            return null;
        }
        if (Instant.now().isAfter(lastUpdated.plus(evictionDuration))) {
            return null;
        }
        return cachedObject;
    }
}
