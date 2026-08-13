package com.socialmedia.backend.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class CommentRepository {

    private final SimpleJdbcCall createCommentCall;

    public CommentRepository(JdbcTemplate jdbcTemplate) {
        this.createCommentCall =
            new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("SP_CREATE_COMMENT");
    }

    public Map<String, Object> createComment(
        Long userId,
        Long postId,
        String content
    ) {

        Map<String, Object> parameters = new HashMap<>();

        parameters.put("P_USER_ID", userId);
        parameters.put("P_POST_ID",postId);
        parameters.put("P_CONTENT",content);

        return createCommentCall.execute(parameters);
    }
}