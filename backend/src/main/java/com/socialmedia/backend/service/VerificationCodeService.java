package com.socialmedia.backend.service;

import com.socialmedia.backend.exception.CustomException;
import com.socialmedia.backend.repository.VerificationCodeRepository;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class VerificationCodeService {
    
    private static final String PURPOSE_LOGIN = "LOGIN";
    
    private final VerificationCodeRepository repository;
    private final PasswordEncoder passwordEncoder;

    private final SecureRandom secureRandom = new SecureRandom();

    public VerificationCodeService (
        VerificationCodeRepository repository,
        PasswordEncoder passwordEncoder
    ) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public String createLoginOtp(Long userId) {

        String otp = String.format("%06d", secureRandom.nextInt(1_000_000));

        String codeHash = passwordEncoder.encode(otp);

        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(5);

        Map<String, Object> result =
            repository.create (
                userId,
                PURPOSE_LOGIN,
                codeHash,
                expiresAt
            );
        
        Number resultCode = (Number) result.get("P_RESULT");

        if (resultCode == null || resultCode.intValue() != 1) {
            throw new CustomException("驗證碼產生失敗", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // test OTP
        System.out.println(
            "=== DEV OTP === User ID: "
            + userId
            + ", OTP: "
            + otp
        );

        return otp;
    }

    public void verifyLoginOtp(Long userId, String otp) {

        Map<String, Object> result =
            repository.getActive(userId, PURPOSE_LOGIN);
        
        Number resultCode = (Number) result.get("P_RESULT");

        if (resultCode == null) {
            throw new CustomException("驗證碼驗證失敗", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (resultCode.intValue() == 0) {
            throw new CustomException("驗證碼不存在或已過期", HttpStatus.UNAUTHORIZED);
        }

        if (resultCode.intValue() == -1) {
            throw new CustomException("驗證碼驗證失敗", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Number verificationId = (Number) result.get("P_VERIFICATION_ID");

        String codeHash = (String) result.get("P_CODE_HASH");

        if (verificationId == null || codeHash == null) {
            throw new CustomException("驗證碼驗證失敗", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        boolean matches = passwordEncoder.matches(otp, codeHash);

        if (!matches) {
            throw new CustomException("驗證碼錯誤", HttpStatus.UNAUTHORIZED);
        }

        Map<String, Object> consumeResult =
            repository.consume(verificationId.longValue(), userId);
        
        Number consumeCode = (Number) consumeResult.get("P_RESULT");

        if (consumeCode == null || consumeCode.intValue() != 1) {
            throw new CustomException("驗證碼使用失敗", HttpStatus.UNAUTHORIZED);
        }
    }
}
