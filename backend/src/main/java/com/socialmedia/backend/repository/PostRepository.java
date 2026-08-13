package com.socialmedia.backend.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Repository
public class PostRepository {

    private final SimpleJdbcCall createPostCall;
    private final SimpleJdbcCall getAllPostsCall;
    private final SimpleJdbcCall updatePostCall;
    private final SimpleJdbcCall deletePostCall;

    public PostRepository(JdbcTemplate jdbcTemplate) {

        this.createPostCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("SP_CREATE_POST");

        this.getAllPostsCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("SP_GET_ALL_POSTS")
                .returningResultSet(
                    "P_POSTS",
                    (rs, rowNum) -> Map.of(
                        "POST_ID", rs.getLong("POST_ID"),
                        "USER_ID", rs.getLong("USER_ID"),
                        "USER_NAME", rs.getString("USER_NAME"),
                        "CONTENT", rs.getString("CONTENT"),
                        "IMAGE", rs.getString("IMAGE"),
                        "CREATED_AT", rs.getTimestamp("CREATED_AT"),
                        "UPDATED_AT", rs.getTimestamp("UPDATED_AT")
                    )
                );

        this.updatePostCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("SP_UPDATE_POST");
        this.deletePostCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("SP_DELETE_POST");
    }

    public Map<String, Object> createPost(
        Long userId,
        String content,
        String image
    ) {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("P_USER_ID", userId);
        parameters.put("P_CONTENT", content);
        parameters.put("P_IMAGE", image);

        return createPostCall.execute(parameters);
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getAllPosts() {

        Map<String, Object> result =
            getAllPostsCall.execute();

        return (List<Map<String, Object>>)
            result.get("P_POSTS");
    }

    public Map<String, Object> updatePost(
        Long postId,
        Long userId,
        String content,
        String image
    ) {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("P_POST_ID", postId);
        parameters.put("P_USER_ID", userId);
        parameters.put("P_CONTENT", content);
        parameters.put("P_IMAGE", image);

        return updatePostCall.execute(parameters);
    }

    public Map<String, Object> deletePost(
        Long postId,
        Long userId
    ) {

        return deletePostCall.execute(
            Map.of(
                "P_POST_ID", postId,
                "P_USER_ID", userId
            )
        );
    }
}