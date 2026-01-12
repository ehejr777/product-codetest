package com.wjc.codetest.product.controller;

import com.wjc.codetest.global.response.ApiResponse;
import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.model.request.CreateProductRequest;
import com.wjc.codetest.product.model.request.GetProductListRequest;
import com.wjc.codetest.product.model.request.UpdateProductRequest;
import com.wjc.codetest.product.model.response.ProductListResponse;
import com.wjc.codetest.product.model.response.ProductResponse;
import com.wjc.codetest.product.service.ProductQueryService;
import com.wjc.codetest.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ProductController
 *
 * [상품 관련 API Controller]
 *
 * 1. 문제
 * - 조회/삭제/수정이 모두 POST 사용
 * - URI에 동사가 포함됨 (/get/product/by/{id}, /create/product 등)
 * - Entity(Product)를 그대로 Response로 반환하여 API 계약 안정성이 낮음
 * - 삭제 API가 Boolean(true)을 반환하여 HTTP 의미가 불명확함
 * - 목록 조회 시 Controller에서 페이징 정보 조립 책임을 가짐
 * 2. 원인
 * - REST API 설계 원칙에 대한 고려 부족
 * - Controller와 DTO의 역할 분리가 명확하지 않음

 * 3. 개선안
 * - 자원 중심 URI + HTTP Method 의미에 맞게 API 재설계
 * - Response 전용 DTO(ProductResponse, ProductListResponse) 도입
 * - 삭제 성공 시 204 No Content 반환
 * - Page 객체를 그대로 Response DTO로 전달하여 응답 조립 책임 이동
 *
 */
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductQueryService productQueryService;

    /**
     * 상품 단건 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "상품 조회 성공",
                        new ProductResponse(productQueryService.getById(id))
                )
        );
    }

    /**
     * 상품 생성
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody CreateProductRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "상품 생성 성공",
                        new ProductResponse(productService.create(request))
                )
        );
    }

    /**
     * 상품 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "상품 수정 성공",
                        new ProductResponse(productService.update(id, request))
                )
        );
    }

    /**
     * 상품 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 상품 목록 조회
     */
    @GetMapping
    public ResponseEntity<ApiResponse<ProductListResponse>> getProductListByCategory(
            @Valid @ModelAttribute GetProductListRequest request
    ) {
        Page<Product> page = productQueryService.getListByCategory(
                request.getCategory(),
                request.getPage(),
                request.getSize()
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "상품 목록 조회 성공",
                        new ProductListResponse(page)
                )
        );
    }

    /**
     * 상품 카테고리 목록 조회
     */
    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<String>>> getProductCategories() {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "상품 카테고리 조회 성공",
                        productService.getUniqueCategories()
                )
        );
    }
}


