package com.example.githubcache.cache;

import java.util.Optional;

/**
 * Interface for a response cache.
 * For now only String key/T type pairs
 */
public interface ResponseCache<T> {

    /**
     * Get the cachemapper for the cache
     * @return the cachemapper if it exists
     */
    CacheMapper<T> getMapper();

    /**
     * Add element to response cache
     * @param key the key of the element
     * @param object the object to add
     */
    void addElement(String key, Object object);

    /**
     * Retrieve element from cache if it exists and is not expired
     * @param key the key to retrieve
     * @param type the type of the object to return
     * @return Optional containing object if found
     * in cache and not expired. Else empty optional
     */
    <S> Optional<S> retrieveElement(String key, Class<S> type);


    // FIXME Remove this...just for debugging
    void printCache();
}