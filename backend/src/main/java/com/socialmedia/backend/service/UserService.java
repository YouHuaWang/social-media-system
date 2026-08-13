package com.socialmedia.backend.service;

import com.socialmedia.backend.dto.response.UserProfileResponse;
import com.socialmedia.backend.exception.CustomException;
import com.socialmedia.backend.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserProfileResponse getMyProfile(Long userId) {

        Map<String, Object> result = userRepository.getUserProfile(userId);

        Number resultCode = (Number) result.get("P_RESULT");

        if (resultCode == null) {
            throw new CustomException(
                "取得使用者資料失敗",
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        if (resultCode.intValue() == 0) {
            throw new CustomException(
                "找不到使用者",
                HttpStatus.NOT_FOUND
            );
        }

        if (resultCode.intValue() == -1) {
            throw new CustomException(
                "取得使用者資料失敗",
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        String userName = (String) result.get("P_USER_NAME");

        String phone = (String) result.get("P_PHONE");

        String email = (String) result.get("P_EMAIL");

        Number phoneVerified = (Number) result.get("P_PHONE_VERIFIED");

        Number emailVerified = (Number) result.get("P_EMAIL_VERIFIED");

        String coverImage = (String) result.get("P_COVER_IMAGE");

        String biography = (String) result.get("P_BIOGRAPHY");

        return new UserProfileResponse(
            userId,
            userName,
            phone,
            email,
            phoneVerified != null && phoneVerified.intValue() == 1,
            emailVerified != null && emailVerified.intValue() == 1,
            coverImage,
            biography
        );
    }
}