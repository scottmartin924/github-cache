package com.example.githubcache.cache;


import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

@Builder
@Getter
@ToString
public class CacheObject {
    //FIXME Fail count?
    // FIXME Not sure we actually need a cacheobjectstatus anymore
    private CacheObjectStatus status;
    private String value;
    private Instant lastUpdated;
}
