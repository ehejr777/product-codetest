package com.wjc.codetest.global.exception;

import com.wjc.codetest.global.response.ApiErrorResponse;
import com.wjc.codetest.product.exception.ProductNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * GlobalExceptionHandler
 *
 * [전역 예외 처리]
 *
 * - 모든 예외를 공통 응답 포맷(ApiErrorResponse)으로 변환
 * - HTTP Status는 ErrorCode 기준으로 명확히 매핑
 * - Controller는 정상 흐름만 처리하도록 단순화
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 비즈니스 예외 처리 (ErrorCode 보유)
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessException(
            BusinessException e
    ) {
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiErrorResponse.of(errorCode));
    }

    /**
     * RequestBody Validation 실패 (400 Bad Request)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e
    ) {
        Map<String, String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (a, b) -> a
                ));

        ErrorCode errorCode = ErrorCode.VALIDATION_ERROR;

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiErrorResponse.of(errorCode, errors));
    }

    /**
     * PathVariable / RequestParam Validation 실패
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(
            ConstraintViolationException e
    ) {
        Map<String, String> errors = new HashMap<>();

        e.getConstraintViolations().forEach(violation -> {
            String field = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            errors.put(field, message);
        });

        ErrorCode errorCode = ErrorCode.VALIDATION_ERROR;

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiErrorResponse.of(errorCode, errors));
    }

    /**
     * 예상하지 못한 예외 (500)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(Exception e) {

        log.error("Unexpected exception occurred", e);

        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiErrorResponse.of(errorCode));
    }
}
