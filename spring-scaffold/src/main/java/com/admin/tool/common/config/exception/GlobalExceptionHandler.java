package com.admin.tool.common.config.exception;

import com.admin.tool.common.model.ApiResponse;
import com.admin.tool.common.model.Code;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    // 종류별 추가
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<String>> handleException(final ApiException err) {
        log.error("Error ApiException");

        // 별도 추가 Body Data 처리

        return ResponseEntity
                .internalServerError()
                .body(ApiResponse.of(Code.FAIL.code, "Game Server Api Exception"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleException(final Exception err) {
        log.error("Error Exception");

        err.printStackTrace();

        return ResponseEntity
                .internalServerError()
                .body(ApiResponse.of(Code.FAIL.code, err.getMessage()));
    }
}
