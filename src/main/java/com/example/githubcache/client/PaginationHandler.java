package com.example.githubcache.client;

import org.springframework.http.HttpHeaders;

import java.util.Arrays;
import java.util.Optional;

public class PaginationHandler {

    private String paginationHeaderName;

    public PaginationHandler(String headerName) {
        this.paginationHeaderName = headerName;
    }

    public Optional<String> determinePageHeader(HttpHeaders headers) {
        final Object paginationHeader = headers.get(paginationHeaderName);
        // If no link header then don't need to paginate
        if (paginationHeader == null) {
            return Optional.empty();
        }

        return parseNextLink(paginationHeader.toString());
    }

    public Optional<String> parseNextLink(String linkHeader) {
        // Could do this with regex but I'm likely to mess it up
        String nextString = "rel=\"next\"";
        String[] links = linkHeader.split(",");
        return Arrays.stream(links)
                .filter(x -> x.contains(nextString))
                .map(x -> {
                    int firstIdx = x.indexOf("<");
                    int lastIdx = x.indexOf(">");
                    return x.substring(firstIdx, lastIdx);
                })
                .findFirst();
    }



}
