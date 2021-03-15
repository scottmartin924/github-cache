package com.example.githubcache.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;

import java.lang.reflect.Type;

/**
 * Mapper for converting to cache objects using jackson. Converts all objects
 * to JSON
 */
public class JacksonCacheMapper implements CacheMapper {
    private static final Logger LOG = LoggerFactory.getLogger(JacksonCacheMapper.class);

    private final ObjectMapper objectMapper;

    public JacksonCacheMapper(ObjectMapper mapper) {
        this.objectMapper = mapper;
    }

    // FIXME Consider how to handle errors in here...this is gross
    @Override
    public String toCacheObject(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException exc) {
            // FIXME Throw custom error
            LOG.error("Unable to parse object when converting to cache object.", exc);
            throw new RuntimeException("Unable to parse object when converting to cache object.", exc);
        }
    }

    @Override
    public <S> S fromCacheObject(String cacheObject, Class<S> returnType) {
        try {
            return objectMapper.readValue(cacheObject, returnType);
        } catch (JsonProcessingException exc) {
            throw new RuntimeException("Unable to parse object from cache type.", exc);
        }
    }

    @Override
    public <S> S fromCacheObject(String cacheObject, ParameterizedTypeReference<S> returnType) {
        try {
            TypeReference<S> tr = new TypeReference() {
                public Type getType() {
                    return returnType.getType();
                }
            };
            return objectMapper.readValue(cacheObject, tr);
        } catch (JsonProcessingException exc) {
            throw new RuntimeException("Unable to parse object from ");
        }
    }
}
