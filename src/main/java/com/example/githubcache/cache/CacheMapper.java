package com.example.githubcache.cache;

import org.springframework.core.ParameterizedTypeReference;

/**
 * Mapper for converting to/from cache object type
 * @param <T> the type of values stored in the cache
 */
public interface CacheMapper<T> {

    /**
     * Convert object to object type for cache
     * @param obj the object to convert
     * @return the object represented as an object type stored in cache
     */
    T toCacheObject(Object obj);

    /**
     * Convert object from cache to non-cache object
     * @param cacheObject the cache object to convert
     * @param returnType the return type of the object
     * @param <S> the type to return
     * @return object of type S
     */
    <S> S fromCacheObject(T cacheObject, Class<S> returnType);

    /**
     * Convert object from cache to non-cache object
     * @param cacheObject the cache object to convert
     * @param returnType the return type of the object
     * @param <S> the type to return
     * @return object of type S
     */
    <S> S fromCacheObject(T cacheObject, ParameterizedTypeReference<S> returnType);
}
