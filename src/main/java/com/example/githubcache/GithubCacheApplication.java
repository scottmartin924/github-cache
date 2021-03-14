package com.example.githubcache;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@SpringBootApplication
@EnableConfigurationProperties
public class GithubCacheApplication implements CommandLineRunner {

	/** TODO Think about
	 * Create custom configuration exception to throw
	 * Custom configure jackson mapper (just to have access to it)
	 * Documentation
	 * Follow hateoas links instead of hardcoding urls
	 * logging!!!!!!!
	 * Use flux/mono throughought (go to reactive web probably...shit is this going to be rough...no time for play)
	 * Should cache be the thing making the cache object or should it be passed to it? I think the former actually, but then should only return strings from cache not cache objects
	 * Handling pagination
	 * Compression (for now no)
	 * Make classes or hashmap responses (for now hashmaps...add complexity as required)...make a repo class at least to filter on??...yeah probably make some classes
	 * Redis shared state
	 * Sort list for each calculation or do it per request (per request for now...depends on how many requests come in)
	 * Should cache always be string/string...it is for now (ideally no...should have CacheObject type an lots of things that extend it)
	 * SECURITY!!
	 **/
	public static void main(String[] args) {
		SpringApplication.run(GithubCacheApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		WebClient client = WebClient.builder()
				.baseUrl("https://api.github.com")
				.build();

		final String orgsString = "parse-community";
		Mono<ResponseEntity<String>> entity = client.get().uri("/orgs/{org}", orgsString)
				.retrieve()
				.toEntity(String.class);
		// Jackson mapper?
		entity.subscribe(x -> {
			System.out.println("Body: " + x.getBody());
			System.out.println("Headers: " + x.getHeaders());
		});
	}
}
