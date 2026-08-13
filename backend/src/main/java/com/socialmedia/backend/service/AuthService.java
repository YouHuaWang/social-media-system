package com.socialmedia.backend.service;

import com.socialmedia.backend.dto.request.LoginRequest;
import com.socialmedia.backend.dto.request.RegisterRequest;
import com.socialmedia.backend.dto.request.VerifyOtpRequest;
import com.socialmedia.backend.dto.response.AuthResponse;
import com.socialmedia.backend.exception.CustomException;
import com.socialmedia.backend.repository.UserRepository;
import com.socialmedia.backend.security.JwtService;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationCodeService verificationCodeService;
    private final JwtService jwtService;

    public AuthService (
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        VerificationCodeService verificationCodeService,
        JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.verificationCodeService = verificationCodeService;
        this.jwtService = jwtService;
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
            throw new CustomException("註冊失敗", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (resultCode.intValue() == 0) {
            throw new CustomException (
                "手機號碼已註冊",
                HttpStatus.CONFLICT
            );
        }

        if (resultCode.intValue() == -2) {
            throw new CustomException (
                "Email已註冊",
                HttpStatus.CONFLICT
            );
        }

        if (resultCode.intValue() != 1) {
            throw new CustomException (
                "註冊失敗",
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        Number userId = (Number) result.get("P_USER_ID");

        if (userId == null) {
            throw new CustomException (
                "註冊失敗",
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        return new AuthResponse(
            userId.longValue(), 
            "註冊成功",
            false,
            null
        );
    }

    public AuthResponse login (LoginRequest request) {

        Map<String, Object> result = 
            userRepository.login (
                request.phone()
            );

        Number resultCode = (Number) result.get("P_RESULT");

        if (resultCode == null) {
            throw new CustomException("登入失敗", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (resultCode.intValue() == 0) {
            throw new CustomException (
                "手機號碼或密碼錯誤",
                HttpStatus.UNAUTHORIZED
            );
        }

        if (resultCode.intValue() != 1) {
            throw new CustomException (
                "登入失敗",
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        Number userId = (Number) result.get("P_USER_ID");

        String passwordHash = (String) result.get("P_PASSWORD_HASH");

        if (userId == null || passwordHash == null) {
            throw new CustomException (
                "登入失敗",
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        boolean passwordMacthes = passwordEncoder.matches(request.password(), passwordHash);

        if (!passwordMacthes) {
            throw new CustomException("手機號碼或密碼錯誤", HttpStatus.UNAUTHORIZED);
        }

        verificationCodeService.createLoginOtp(userId.longValue());

        return new AuthResponse(
            userId.longValue(), 
            "請輸入手機驗證碼",
            true,
            null
        );
    }

    @Transactional
    public AuthResponse verifyOtp (VerifyOtpRequest request) {

        Map<String, Object> result = 
            userRepository.login (
                request.phone()
            );

        Number resultCode = (Number) result.get("P_RESULT");

        if (resultCode == null || resultCode.intValue() != 1) {
            throw new CustomException("使用者不存在", HttpStatus.UNAUTHORIZED);
        }

        Number userId = (Number) result.get("P_USER_ID");

        if (userId == null) {
            throw new CustomException (
                "使用者不存在",
                HttpStatus.UNAUTHORIZED
            );
        }

        verificationCodeService.verifyLoginOtp(userId.longValue(), request.otp());

        String token = jwtService.generateToken(userId.longValue());

        return new AuthResponse(
            userId.longValue(), 
            "登入成功",
            false,
            token
        );
    }
}
