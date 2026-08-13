package com.socialmedia.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/protected")
    public ResponseEntity<Map<String, Object>> protectedApi(
        Authentication authentication
    ) {

        Long userId =
            (Long) authentication.getPrincipal();

        return ResponseEntity.ok(
            Map.of(
                "message", "JWT驗證成功",
                "userId", userId
            )
        );
    }
}