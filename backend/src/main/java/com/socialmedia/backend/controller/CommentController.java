package com.socialmedia.backend.controller;

import com.socialmedia.backend.dto.request.CreateCommentRequest;
import com.socialmedia.backend.dto.response.CommentResponse;
import com.socialmedia.backend.service.CommentService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class CommentController {

    private final CommentService commentService;

    public CommentController(
        CommentService commentService
    ) {
        this.commentService = commentService;
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentResponse>
        createComment(

            @PathVariable
            Long postId,

            Authentication authentication,

            @Valid
            @RequestBody
            CreateCommentRequest request
        ) {

        Long userId = (Long) authentication.getPrincipal();

        CommentResponse response =
            commentService.createComment(
                userId,
                postId,
                request
            );

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }
}