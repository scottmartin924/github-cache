package com.example.githubcache.config;

import com.example.githubcache.client.GithubClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;

@Component
public class CacheUpdateScheduler {

    private final GithubClient tasks;
    private final ApplicationConfiguration configuration;
    private final TaskScheduler scheduler;

    public CacheUpdateScheduler(@Autowired GithubClient tasks,
                                @Autowired ApplicationConfiguration configuration,
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
