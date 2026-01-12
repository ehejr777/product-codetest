package com.wjc.codetest.product.model.response;

import com.wjc.codetest.product.model.domain.Product;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * ProductListResponse
 *
 * [상품 목록 조회 응답 DTO]
 *
 * 1. 문제
 * - 기존 구현에서는 Page<Product> 또는 List<Product>를 그대로 API 응답으로 반환하여
 *   JPA Entity가 외부(API)에 직접 노출되는 문제가 있었음
 * - Entity 구조 변경이 API 스펙 변경으로 직결되고,
 *   Lazy Loading 연관관계가 존재할 경우 N+1 쿼리 또는 직렬화 오류가 발생할 수 있었음
 *
 * 2. 원인
 * - Entity와 Response DTO의 역할 구분이 명확하지 않은 설계
 * - 빠른 구현을 위해 Entity를 응답 객체로 재사용한 구조
 *
 * 3. 개선안
 * - API 응답 전용 DTO(ProductListResponse, ProductResponse) 분리
 * - Page<Product>를 생성자에서 받아 필요한 정보만 추출하여 매핑
 * - Entity를 직접 노출하지 않고, 외부 계약(API 스펙)을 안정적으로 유지
 *
 *   → Entity 변경과 API 변경을 분리하여 유지보수성과 확장성 향상
 *
 * 4. 검증
 * - Entity 필드 추가/변경 시 API 응답 스펙에 영향 없음 확인
 * - Lazy Loading 연관관계로 인한 추가 쿼리 및 직렬화 오류 발생 여부 점검
 */

@Getter
public class ProductListResponse {

    private final List<ProductResponse> products;
    private final int totalPages;
    private final long totalElements;
    private final int page;

    public ProductListResponse(Page<Product> page) {
        this.products = page.getContent()
                .stream()
                .map(ProductResponse::new)
                .toList();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.page = page.getNumber();
    }
}
