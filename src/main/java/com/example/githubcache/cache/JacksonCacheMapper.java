package com.example.githubcache.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;

import java.lang.reflect.Type;

/**
 * Mapper for converting to cache objects using jackson. Converts all objects
 * to JSON
 */
public class JacksonCacheMapper implements CacheMapper {
    private final ObjectMapper objectMapper;

    public JacksonCacheMapper(ObjectMapper mapper) {
        this.objectMapper = mapper;
    }

    @Override
    public String toCacheObject(Object obj) throws CacheMapperException {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException exc) {
            throw new CacheMapperException("Unable to parse when converting to cache object.", exc);
        }
    }

    @Override
    public <S> S fromCacheObject(String cacheObject, Class<S> returnType) throws CacheMapperException {
        try {
            return objectMapper.readValue(cacheObject, returnType);
        } catch (JsonProcessingException exc) {
            throw new CacheMapperException("Unable to parse when converting to cache object.", exc);
        }
    }

    @Override
    public <S> S fromCacheObject(String cacheObject, ParameterizedTypeReference<S> returnType) throws CacheMapperException {
        try {
            TypeReference<S> tr = new TypeReference() {
                public Type getType() {
                    return returnType.getType();
                }
            };
            return objectMapper.readValue(cacheObject, tr);
        } catch (JsonProcessingException exc) {
            throw new CacheMapperException("Unable to parse when converting to cache object.", exc);
        }
    }
}
