package com.wjc.codetest.product.model.response;

import com.wjc.codetest.product.model.domain.Product;
import lombok.Getter;

@Getter
public class ProductResponse {
    private final Long id;
    private final String category;
    private final String name;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.category = product.getCategory();
        this.name = product.getName();
    }
}
