package com.wjc.codetest.product.service;

import com.wjc.codetest.product.exception.ProductNotFoundException;
import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 상품 단건 조회
 *
 * 1. 문제
 * - Optional을 직접 분기 처리하여 코드 가독성이 떨어짐
 * - RuntimeException 사용으로 예외의 의미가 불명확함
 * - 조회 실패 시 어떤 HTTP 상태로 변환될지 예측하기 어려움
 *
 * 2. 원인
 * - Optional의 의도(orElseThrow)를 충분히 활용하지 못한 구현
 * - 도메인 예외 정의 및 예외 처리 전략 부재
 *
 * 3. 개선안
 * - Optional.orElseThrow()를 사용하여 코드 단순화
 * - 의미 있는 도메인 예외(ProductNotFoundException)로 명확한 실패 원인 표현
 * - 상위(GlobalExceptionHandler)에서 HTTP 404로 매핑 가능하도록 설계
 *
 * 4. 검증
 * - 존재하지 않는 ID 조회 시 ProductNotFoundException 발생 확인
 * - 정상 ID 조회 시 기존 동작 동일함 확인
 */

@Service
@RequiredArgsConstructor
public class ProductQueryService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Product getById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Page<Product> getListByCategory(
            String category,
            int page,
            int size
    ) {
        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "id")
        );

        if (category == null || category.isBlank()) {
            return productRepository.findAll(pageRequest);
        }

        return productRepository.findAllByCategory(category, pageRequest);
    }
}
