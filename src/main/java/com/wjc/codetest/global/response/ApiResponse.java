package com.wjc.codetest.global.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {

    private final String code;
    private final String message;
    private final T data;

    /* 기본 성공 */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(
                "SUCCESS",
                "요청이 성공적으로 처리되었습니다.",
                data
        );
    }

    /* 메시지 커스터마이징 */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(
                "SUCCESS",
                message,
                data
        );
    }
}
