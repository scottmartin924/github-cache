package com.example.githubcache.config;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Task list
 */
public class EndpointTaskList {
    private List<Runnable> tasks;

    private EndpointTaskList(Builder builder) {
        this.tasks = builder.tasks;
    }

    /**
     * Execute task list
     * @param org
     */
    public void executeTaskList(final String org) {
        tasks.forEach(Runnable::run);
    }

    /**
     * Builder class
     */
    public static class Builder {
        private List<Runnable> tasks = new ArrayList<>();

        /**
         * Add task to list of tasks for builder
         * @param task the task to add
         * @return the builder
         */
        public Builder addTask(Runnable task) {
            this.tasks.add(task);
            return this;
        }

        /**
         * Build endpoint task list
         * @return the endpoint task list
         */
        public EndpointTaskList build() {
            return new EndpointTaskList(this);
        }
    }
}
