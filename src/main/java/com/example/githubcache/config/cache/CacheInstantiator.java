package com.example.githubcache.config.cache;

import com.example.githubcache.cache.JacksonCacheMapper;
import com.example.githubcache.cache.RedisCache;
import com.example.githubcache.cache.ResponseCache;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

/**
 * Class to create cache bean and
 * its dependencies
 */
@Configuration
public class CacheInstantiator {

    /**
     * Create jackson object mapper
     *
     * @return a configured object mapper
     */
    @Bean
    public ObjectMapper mapper() {
        return new ObjectMapper()
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
    }

    /**
     * Create response cache. In this case a RedisResponseCache
     *
     * @param cacheConfig the configuration for the cache
     * @param mapper      the object mapper
     * @return configured cache
     */
    @Bean
    public ResponseCache cache(@Autowired CacheConfiguration cacheConfig,
                               @Autowired ObjectMapper mapper) {
        Jedis jedis = new Jedis(cacheConfig.getRedis().getHost());
        JacksonCacheMapper jacksonCacheMapper = new JacksonCacheMapper(mapper);
        return new RedisCache(jedis, cacheConfig.getEvictionTime(), jacksonCacheMapper);
    }
}
