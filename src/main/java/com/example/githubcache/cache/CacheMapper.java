package com.example.githubcache.cache;

import org.springframework.core.ParameterizedTypeReference;

/**
 * Mapper for converting to/from cache object type
 */
public interface CacheMapper {

    /**
     * Convert object to object type for cache
     *
     * @param obj the object to convert
     * @return the object represented as an object type stored in cache
     * @throws CacheMapperException if error occurred while mapping
     */
    String toCacheObject(Object obj) throws CacheMapperException;

    /**
     * Convert object from cache to non-cache object
     *
     * @param cacheObject the cache object to convert
     * @param returnType  the return type of the object
     * @param <S>         the type to return
     * @return object of type S
     * @throws CacheMapperException if error occurred while mapping
     */
    <S> S fromCacheObject(String cacheObject, Class<S> returnType) throws CacheMapperException;

    /**
     * Convert object from cache to non-cache object
     *
     * @param cacheObject the cache object to convert
     * @param returnType  the return type of the object
     * @param <S>         the type to return
     * @return object of type S
     * @throws CacheMapperException if error occurred while mapping
     */
    <S> S fromCacheObject(String cacheObject, ParameterizedTypeReference<S> returnType) throws CacheMapperException;
}
