package com.socialmedia.backend.exception;

import com.socialmedia.backend.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomException (
        CustomException e
    ) {

        return ResponseEntity
            .status(e.getStatus())
            .body(
                ApiResponse.fail(
                    e.getStatus().value(),
                    e.getMessage()
                )
            );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException (
        Exception e
    ) {

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ApiResponse.fail(
                    500,
                    "系統發生錯誤"
                )
            );
    }
}