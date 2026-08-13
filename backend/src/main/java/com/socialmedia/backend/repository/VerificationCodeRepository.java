package com.socialmedia.backend.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Map;

@Repository
public class VerificationCodeRepository {
    
    private final SimpleJdbcCall createCodeCall;
    private final SimpleJdbcCall getActiveCodeCall;
    private final SimpleJdbcCall consumeCodeCall;

    public VerificationCodeRepository(JdbcTemplate jdbcTemplate) {

        this.createCodeCall = 
            new SimpleJdbcCall(jdbcTemplate).withProcedureName("SP_CREATE_OTP");

        this.getActiveCodeCall = 
            new SimpleJdbcCall(jdbcTemplate).withProcedureName("SP_GET_ACTIVE_OTP");
        
        this.consumeCodeCall = 
            new SimpleJdbcCall(jdbcTemplate).withProcedureName("SP_CONSUME_OTP");
    }

    // Create OTP
    public Map<String, Object> create (
        Long userId,
        String purpose,
        String codeHash,
        LocalDateTime expiresAt
    ) {
        return createCodeCall.execute (
            Map.of(
                "P_USER_ID", userId, 
                "P_PURPOSE", purpose,
                "P_CODE_HASH", codeHash,
                "P_EXPIRES_AT", expiresAt
            )
        );
    }

    // Get Active OTP
    public Map<String, Object> getActive (
        Long userId,
        String purpose
    ) {
        return getActiveCodeCall.execute (
            Map.of(
                "P_USER_ID", userId, 
                "P_PURPOSE", purpose
            )
        );
    }

    // Consume OTP
    public Map<String, Object> consume (
        Long verificationId,
        Long userId
    ) {
        return consumeCodeCall.execute (
            Map.of(
                "P_VERIFICATION_ID", verificationId, 
                "P_USER_ID", userId
            )
        );
    }
}
