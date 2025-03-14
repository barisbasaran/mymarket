package com.mymarket.productcategory;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductCategory {

    private Long id;

    private boolean active;

    @Size(min = 1, max = 50, message = "{error.product-category.name}")
    private String name;

    @Size(max = 5000, message = "{error.product-category.description}")
    private String description;

    private ProductCategory parent;

    private List<ProductCategory> potentialParents;
}
