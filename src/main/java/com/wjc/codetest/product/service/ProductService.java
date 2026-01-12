package com.wjc.codetest.product.service;

import com.wjc.codetest.product.model.request.CreateProductRequest;
import com.wjc.codetest.product.model.request.GetProductListRequest;
import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.model.request.UpdateProductRequest;
import com.wjc.codetest.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductQueryService productQueryService;
    private final ProductRepository productRepository;

    /**
     * 상품 생성
     *
     * 1. 문제
     * - 트랜잭션 경계가 명확하지 않아 저장 도중 예외 발생 시 일관성 보장이 불명확함
     * - 생성 로직의 의도가 코드상에서 명확히 드러나지 않음
     * - 로깅이 없어 운영 중 문제 추적이 어려움
     *
     * 2. 원인
     * - Service 계층에서 트랜잭션과 책임 범위 정의 부족
     *
     * 3. 개선안
     * - @Transactional 적용으로 저장 로직의 원자성 보장
     * - Entity 생성 책임을 Service에 명확히 둠
     * - 주요 비즈니스 이벤트(상품 생성)에 대한 로그 추가
     *
     * 4. 검증
     * - 정상 요청 시 상품 생성 및 저장 정상 동작 확인
     * - 예외 발생 시 트랜잭션 롤백 여부 확인
     */
    @Transactional
    public Product create(CreateProductRequest dto) {
        Product product = new Product(dto.getCategory(), dto.getName());
        Product savedProduct = productRepository.save(product);

        log.info("Product created. id={}, category={}, name={}",
                savedProduct.getId(),
                savedProduct.getCategory(),
                savedProduct.getName()
        );

        return savedProduct;
    }

    /**
     * 상품 수정
     *
     * - Dirty Checking을 활용하여 save() 호출 없이 수정 반영
     */
    @Transactional
    public Product update(Long productId, UpdateProductRequest dto) {
        Product product = productQueryService.getById(productId);
        product.change(dto.getCategory(), dto.getName());

        return product; // Dirty Checking
    }


    @Transactional
    public void deleteById(Long productId) {
        Product product = productQueryService.getById(productId);
        productRepository.delete(product);
    }
}