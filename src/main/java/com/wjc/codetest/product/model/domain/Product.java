package com.wjc.codetest.product.model.domain;

import jakarta.persistence.*;
import lombok.Getter;
import java.util.Objects;

/**
 *  Entity 설계 문제 (Product)
 *
 * 1. 문제
 * - Product Entity에 @Setter가 열려 있어 모든 필드가 무제한 변경 가능함
 * - Entity가 단순 DTO처럼 사용되어 비즈니스 상태 보호가 되지 않음
 * - null, 빈 값 등 비정상 상태의 Product가 언제든 생성/변경될 수 있음
 *
 * 2. 원인
 * - JPA Entity와 Request/Response DTO의 역할 분리가 명확하지 않음
 * - Entity를 "데이터 컨테이너"로 인식하여 캡슐화가 깨진 설계
 *
 * 3. 개선안
 * - Entity에서 @Setter 제거
 * - 생성자에서 필수 값(category, name)을 강제하여 불완전한 상태 생성 방지
 * - 변경 로직을 의미 있는 메서드(change)로 제한하여 상태 변경 지점 통제
 *
 *   → Entity는 스스로 자신의 상태를 보호하도록 설계
 *
 * 4. 검증 (생략)
 * - 컴파일 단계에서 setter 사용 불가 확인
 * - 변경 로직이 명시적인 메서드 호출로만 발생함을 코드 레벨에서 확인
 */

@Entity
@Getter
@Table(
        name = "product",
        indexes = {
                @Index(name = "idx_product_category", columnList = "category")
        }
)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(nullable = false, length = 100)
    private String name;

    protected Product() {}

    public Product(String category, String name) {
        this.category = Objects.requireNonNull(category);
        this.name = Objects.requireNonNull(name);
    }

    public void change(String category, String name) {
        this.category = Objects.requireNonNull(category);
        this.name = Objects.requireNonNull(name);
    }
}
