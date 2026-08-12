package com.socialmedia.backend.service;

import com.socialmedia.backend.dto.request.RegisterRequest;
import com.socialmedia.backend.dto.response.AuthResponse;
import com.socialmedia.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService (
        UserRepository userRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AuthResponse register (RegisterRequest request) {

        String passwordHash = passwordEncoder.encode(request.password());

        Map<String, Object> result = 
            userRepository.register (
                request.userName(),
                request.phone(),
                request.email(),
                passwordHash
            );

        Number resultCode = (Number) result.get("P_RESULT");

        if (resultCode == null) {
            throw new RuntimeException("註冊失敗");
        }

        if (resultCode.intValue() == 0) {
            throw new RuntimeException("手機號碼已註冊");
        }

        if (resultCode.intValue() == -2) {
            throw new RuntimeException("Email已註冊");
        }

        Number userId = (Number) result.get("P_USER_ID");

        return new AuthResponse(
            userId.longValue(), "註冊成功"
        );
    }
}
