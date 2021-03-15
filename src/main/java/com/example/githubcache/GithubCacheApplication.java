package com.example.githubcache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class GithubCacheApplication {
	public static void main(String[] args) {
		SpringApplication.run(GithubCacheApplication.class, args);
	}
}
