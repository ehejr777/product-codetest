package com.wjc.codetest.product.exception;

import com.wjc.codetest.global.exception.BusinessException;
import com.wjc.codetest.global.exception.ErrorCode;

/**
 * ProductNotFoundException
 *
 * - Product 도메인 조회 실패 시 발생하는 예외
 * - Service 계층에서 발생시켜 Controller까지 전파
 */
public class ProductNotFoundException extends BusinessException {

    public ProductNotFoundException() {
        super(ErrorCode.PRODUCT_NOT_FOUND);
    }
}
