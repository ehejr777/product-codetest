package com.wjc.codetest.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * ErrorCode
 *
 * [에러 코드 정의]
 */
@Getter
public enum ErrorCode {

    // 400
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "COMMON-001", "잘못된 요청입니다."),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "COMMON-002", "요청 값 검증에 실패했습니다."),

    // 404
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "PRODUCT-001", "상품을 찾을 수 없습니다."),

    // 500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON-999", "서버 내부 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
