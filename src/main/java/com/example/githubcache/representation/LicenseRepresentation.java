package com.example.githubcache.representation;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
public class LicenseRepresentation {
    private String key;
    private String name;
    private String spdxId;
    private String url;
    private String nodeId;
}
