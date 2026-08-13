package com.socialmedia.backend.service;

import com.socialmedia.backend.common.HtmlSanitizer;
import com.socialmedia.backend.dto.request.CreatePostRequest;
import com.socialmedia.backend.dto.request.UpdatePostRequest;
import com.socialmedia.backend.dto.response.PostResponse;
import com.socialmedia.backend.exception.CustomException;
import com.socialmedia.backend.repository.PostRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final HtmlSanitizer htmlSanitizer;

    public PostService(
        PostRepository postRepository,
        HtmlSanitizer htmlSanitizer
    ) {
        this.postRepository = postRepository;
        this.htmlSanitizer = htmlSanitizer;
    }

    @Transactional
    public PostResponse createPost(
        Long userId,
        CreatePostRequest request
    ) {

        String content =
            htmlSanitizer.sanitize(
                request.content()
            );

        Map<String, Object> result =
            postRepository.createPost(
                userId,
                content,
                request.image()
            );

        Number resultCode = (Number) result.get("P_RESULT");

        if (resultCode == null || resultCode.intValue() != 1) {
            throw new CustomException("發文失敗", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Number postId = (Number) result.get("P_POST_ID");

        return new PostResponse(
            postId.longValue(),
            userId,
            null,
            content,
            request.image(),
            null,
            null
        );
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {

        List<Map<String, Object>> posts = postRepository.getAllPosts();

        return posts.stream()
            .map(this::toPostResponse)
            .toList();
    }

    @Transactional
    public void updatePost(
        Long postId,
        Long userId,
        UpdatePostRequest request
    ) {

        String content = htmlSanitizer.sanitize(request.content());

        Map<String, Object> result =
            postRepository.updatePost(
                postId,
                userId,
                content,
                request.image()
            );

        Number resultCode = (Number) result.get("P_RESULT");

        if (resultCode == null || resultCode.intValue() == -1) {
            throw new CustomException("編輯失敗", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (resultCode.intValue() == 0) {

            throw new CustomException("找不到貼文或您沒有修改權限", HttpStatus.FORBIDDEN);
        }
    }

    @Transactional
    public void deletePost(
        Long postId,
        Long userId
    ) {

        Map<String, Object> result =
            postRepository.deletePost(
                postId,
                userId
            );

        Number resultCode = (Number) result.get("P_RESULT");

        if (resultCode == null || resultCode.intValue() == -1) {
            throw new CustomException("刪除貼文失敗", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (resultCode.intValue() == 0) {
            throw new CustomException("找不到貼文或您沒有刪除權限", HttpStatus.FORBIDDEN);
        }
    }

    private PostResponse toPostResponse(
        Map<String, Object> row
    ) {

        Timestamp createdAt = (Timestamp) row.get("CREATED_AT");
        Timestamp updatedAt = (Timestamp) row.get("UPDATED_AT");

        return new PostResponse(
            ((Number) row.get("POST_ID")).longValue(),
            ((Number) row.get("USER_ID")).longValue(),
            (String) row.get("USER_NAME"),
            (String) row.get("CONTENT"),
            (String) row.get("IMAGE"),

            createdAt != null
                ? createdAt.toLocalDateTime()
                : null,

            updatedAt != null
                ? updatedAt.toLocalDateTime()
                : null
        );
    }
}