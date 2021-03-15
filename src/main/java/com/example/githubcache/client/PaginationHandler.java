package com.example.githubcache.client;

import org.springframework.http.HttpHeaders;

import java.util.Arrays;
import java.util.Optional;

public class PaginationHandler {

    private String paginationHeaderName;

    public PaginationHandler(String headerName) {
        this.paginationHeaderName = headerName;
    }

    /**
     * Find url of next page
     *
     * @param headers the http headers
     * @return string with next page url if next page found. Else null
     */
    public String nextPageUrl(HttpHeaders headers) {
        final Object paginationHeader = headers.get(paginationHeaderName);
        // If no link header then don't need to paginate
        // Should be using an optional
        if (paginationHeader == null) {
            return null;
        }
        return parseNextLink(paginationHeader.toString()).orElse(null);
    }

    /**
     * Parse the next link from string header
     *
     * @param linkHeader the header that the links are on
     * @return string representing the url for the next page
     */
    private Optional<String> parseNextLink(String linkHeader) {
        // Could do this with regex but I'm likely to mess it up
        String nextString = "rel=\"next\"";
        String[] links = linkHeader.split(",");
        return Arrays.stream(links)
                .filter(x -> x.contains(nextString))
                .map(x -> {
                    int firstIdx = x.indexOf("<");
                    int lastIdx = x.indexOf(">");
                    return x.substring(firstIdx + 1, lastIdx);
                })
                .findFirst();
    }
}
