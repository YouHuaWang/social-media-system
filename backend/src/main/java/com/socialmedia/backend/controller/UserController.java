package com.socialmedia.backend.controller;

import com.socialmedia.backend.dto.response.UserProfileResponse;
import com.socialmedia.backend.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getMyProfile(Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName());
        UserProfileResponse response = userService.getMyProfile(userId);
        return ResponseEntity.ok(response);
    }
}