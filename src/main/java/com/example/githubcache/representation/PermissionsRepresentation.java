package com.example.githubcache.representation;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
public class PermissionsRepresentation {
    private boolean admin;
    private boolean push;
    private boolean pull;
}
