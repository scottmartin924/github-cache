package com.example.githubcache.config.cache;

import com.example.githubcache.client.EndpointUpdateClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;

@Component
public class CacheUpdateScheduler {
    private final EndpointUpdateClient tasks;
    private final CacheConfiguration configuration;
    private final TaskScheduler scheduler;

    public CacheUpdateScheduler(@Autowired EndpointUpdateClient tasks,
                                @Autowired CacheConfiguration configuration,
                                @Autowired TaskScheduler scheduler) {
        this.tasks = tasks;
        this.configuration = configuration;
        this.scheduler = scheduler;
    }

    /**
     * Create schedule to update cache
     */
    @PostConstruct
    private void createCacheUpdateSchedule() {
        final Duration scheduleRate = Duration.ofMinutes(configuration.getEvictionTime());
        scheduler.scheduleWithFixedDelay(tasks::execute, scheduleRate);
    }
}
