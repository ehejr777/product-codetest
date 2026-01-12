package com.wjc.codetest.product.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

/**
 * CreateProductRequest
 *
 * [Request DTO Validation 적용]
 *
 * 1. 문제
 * - 기존 요청 DTO에 유효성 검증이 없어 category, name이 null 또는 빈 값("")으로도
 *   서비스 로직까지 전달될 수 있었음
 * - 잘못된 요청이 DB 저장 단계까지 진행될 위험이 있었음
 *
 * 2. 원인
 * - Controller 단에서 입력값 검증(@Valid, Bean Validation) 미적용
 * - Request DTO를 단순 데이터 전달 객체로만 사용한 설계
 *
 * 3. 개선안
 * - @NotBlank를 사용하여 null, 빈 문자열, 공백 입력을 사전에 차단
 * - 유효하지 않은 요청은 Controller 단계에서 400 Bad Request로 즉시 응답
 * - Service 로직은 항상 정상 데이터만 들어온다는 가정 하에 단순화
 *
 * 4. 검증
 * - category 또는 name이 빈 값일 경우 400 Bad Request 반환 확인
 * - 정상 입력 시 기존 로직 정상 동작 확인
 */

@Getter
public class CreateProductRequest {

    @NotBlank
    private String category;

    @NotBlank
    private String name;
}
