package com.socialmedia.backend.controller;

import com.socialmedia.backend.dto.request.CreatePostRequest;
import com.socialmedia.backend.dto.request.UpdatePostRequest;
import com.socialmedia.backend.dto.response.PostResponse;
import com.socialmedia.backend.service.PostService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(
        PostService postService
    ) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(

        Authentication authentication,

        @Valid
        @RequestBody
        CreatePostRequest request
    ) {

        Long userId = (Long) authentication.getPrincipal();

        PostResponse response =
            postService.createPost(
                userId,
                request
            );

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>>
        getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Void> updatePost(

        @PathVariable
        Long postId,

        Authentication authentication,

        @Valid
        @RequestBody
        UpdatePostRequest request
    ) {

        Long userId = (Long) authentication.getPrincipal();

        postService.updatePost(
            postId,
            userId,
            request
        );

        return ResponseEntity
            .noContent()
            .build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(

        @PathVariable
        Long postId,

        Authentication authentication
    ) {

        Long userId = (Long) authentication.getPrincipal();

        postService.deletePost(
            postId,
            userId
        );

        return ResponseEntity.noContent()
            .build();
    }
}