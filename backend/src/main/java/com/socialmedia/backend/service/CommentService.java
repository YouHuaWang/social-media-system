package com.socialmedia.backend.service;

import com.socialmedia.backend.common.HtmlSanitizer;
import com.socialmedia.backend.dto.request.CreateCommentRequest;
import com.socialmedia.backend.dto.response.CommentResponse;
import com.socialmedia.backend.exception.CustomException;
import com.socialmedia.backend.repository.CommentRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final HtmlSanitizer htmlSanitizer;

    public CommentService(
        CommentRepository commentRepository,
        HtmlSanitizer htmlSanitizer
    ) {
        this.commentRepository = commentRepository;
        this.htmlSanitizer = htmlSanitizer;
    }

    @Transactional
    public CommentResponse createComment(
        Long userId,
        Long postId,
        CreateCommentRequest request
    ) {

        String content = htmlSanitizer.sanitize(request.content());

        Map<String, Object> result =
            commentRepository.createComment(
                userId,
                postId,
                content
            );

        Number resultCode = (Number) result.get("P_RESULT");

        if (resultCode == null) {
            throw new CustomException("留言失敗", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (resultCode.intValue() == 0) {
            throw new CustomException("找不到該貼文", HttpStatus.NOT_FOUND);
        }

        if (resultCode.intValue() == -1) {
            throw new CustomException("留言失敗", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Number commentId = (Number) result.get("P_COMMENT_ID");

        return new CommentResponse(
            commentId.longValue(),
            postId,
            userId,
            "留言成功"
        );
    }
}