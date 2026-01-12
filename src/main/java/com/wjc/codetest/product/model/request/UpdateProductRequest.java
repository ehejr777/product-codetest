package com.wjc.codetest.product.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

/**
 * UpdateProductRequest
 *
 * [상품 수정 요청 DTO]
 *
 * 1. 문제
 * - category, name에 대한 유효성 검증이 없어 null 또는 빈 값으로 수정 요청 가능
 * - 생성자가 여러 개 존재하여 어떤 필드가 필수인지 명확하지 않음
 * - @Setter 사용으로 객체 상태가 어디서든 변경 가능하여 요청 데이터의 신뢰성이 낮음
 *
 * 2. 원인
 * - Request DTO를 단순 데이터 컨테이너로 설계
 * - Validation 책임을 Service 또는 Entity에 위임한 구조
 *
 * 3. 개선안
 * - @NotNull, @NotBlank를 사용하여 수정에 필요한 필수 값 명확화
 * - 불필요한 생성자 제거 → 기본 생성자 + JSON 바인딩만 허용
 * - @Setter 제거로 요청 객체를 불변에 가깝게 유지
 *
 *   → Controller 단에서 잘못된 수정 요청을 사전에 차단
 *
 * 4. 검증
 * - id가 null이거나 category/name이 빈 값일 경우 400 Bad Request 반환 확인
 * - 정상 요청 시 기존 수정 로직 정상 동작 확인
 */
@Getter
public class UpdateProductRequest {

    @NotBlank
    private String category;

    @NotBlank
    private String name;
}
