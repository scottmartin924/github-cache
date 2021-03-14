package com.example.githubcache.cache;

import com.fasterxml.jackson.databind.JsonNode;
import redis.clients.jedis.Jedis;

import java.util.Optional;

public class RedisCache implements ResponseCache {
    private final Jedis jedis;
    private final int evictionTime;

    public RedisCache(Jedis jedis, int evictionTime) {
        this.jedis = jedis;
        this.evictionTime = evictionTime;
    }

    @Override
    public void addElement(String key, JsonNode object) {

    }

    @Override
    public Optional<JsonNode> retrieveElement(String key) {
        return Optional.empty();
    }

    @Override
    public int getEvictionTime() {
        return this.evictionTime;
    }

    @Override
    public void printCache() {

    }
}
