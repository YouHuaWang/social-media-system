package com.socialmedia.backend.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class UserRepository {
    
    private final SimpleJdbcCall registerCall;
    private final SimpleJdbcCall loginCall;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.registerCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("SP_REGISTER");
        this.loginCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("SP_LOGIN");
    }

    public Map<String, Object> register (
        String userName,
        String phone,
        String email,
        String passwordHash
    ) {
        return registerCall.execute (
            Map.of(
                "P_USER_NAME", userName,
                "P_PHONE", phone,
                "P_EMAIL", email,
                "P_PASSWORD_HASH", passwordHash
            )
        );
    }

    public Map<String, Object> login (String phone) {
        return loginCall.execute (
            Map.of(
                "P_PHONE", phone
            )
        );
    }
}
