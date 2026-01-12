package com.wjc.codetest.global.response;

import com.wjc.codetest.global.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiErrorResponse {

    private final String code;
    private final String message;
    private final Map<String, String> errors;
    private final LocalDateTime timestamp;

    public static ApiErrorResponse of(ErrorCode errorCode) {
        return new ApiErrorResponse(
                errorCode.getCode(),
                errorCode.getMessage(),
                null,
                LocalDateTime.now()
        );
    }

    public static ApiErrorResponse of(ErrorCode errorCode, Map<String, String> errors) {
        return new ApiErrorResponse(
                errorCode.getCode(),
                errorCode.getMessage(),
                errors,
                LocalDateTime.now()
        );
    }
}
