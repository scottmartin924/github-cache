package com.example.githubcache.client;

/**
 * Interface encapsulating the idea of
 * pulling a resource from a remote location
 * via http request
 */
public interface WebApiClient {

    /**
     * Get remote resource from external location via
     * http request. Note that the location should be an extension
     * of the base path which is configured on the base
     * WebClient object
     *
     * @param location   the extension to pull the resource from
     * @param returnType the type of the return object deserialized
     *                   by a jackson mapper
     * @param <T>        the type to return from the remote
     * @return the response of the remote endpoint (mapped to type T via jackson)
     */
    <T> T getResource(String location, Class<T> returnType);
}
