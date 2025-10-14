package com.example.hello.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.example.hello.dto.Post;

@Service
public class PostService {

    private final RestClient restClient;

	public PostService(@Value("${external.posts.base-url}") String postsBaseUrl) {
		this.restClient = RestClient.builder()
			.baseUrl(postsBaseUrl)
			.build();
	}

	public List<Post> fetchTopHundredPostsByUser(long userId) {
		Post[] posts = this.restClient.get()
			.uri(uriBuilder -> uriBuilder
				.path("/posts")
				.queryParam("userId", userId)
				.build())
			.accept(MediaType.APPLICATION_JSON)
			.retrieve()
			.body(Post[].class);
		
		if (posts == null) {
			return List.of();
		}
		
		return Arrays.stream(posts)
			.limit(100)
			.collect(Collectors.toList());
	}
}


