package com.example.hello.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hello.dto.Post;
import com.example.hello.service.PostService;

@RestController
@RequestMapping("/api/users/{userId}/posts")
public class PostsController {

	private final PostService postService;

	public PostsController(PostService postService) {
		this.postService = postService;
	}

	@GetMapping("/top100")
	public ResponseEntity<List<Post>> getTopHundred(@PathVariable("userId") long userId) {
		List<Post> posts = postService.fetchTopHundredPostsByUser(userId);
		return ResponseEntity.ok(posts);
	}
}


