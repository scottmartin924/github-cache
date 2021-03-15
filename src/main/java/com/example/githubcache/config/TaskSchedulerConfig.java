package com.example.githubcache.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class TaskSchedulerConfig {

    /**
     * Create task scheduler
     *
     * @return configured task scheduled
     */
    @Bean
    public TaskScheduler scheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        // NOTE: Could probably get away with a size 1 pool here since it's likely this won't run often, but 2 seems fine
        scheduler.setPoolSize(2);
        scheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
        return scheduler;
    }
}
