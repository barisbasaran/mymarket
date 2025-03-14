package com.mymarket.productcategory;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductCategoryMapper {

    public ProductCategoryEntity toEntity(ProductCategory productCategory) {
        if (productCategory == null) {
            return null;
        }
        return ProductCategoryEntity.builder()
            .id(productCategory.getId())
            .active(productCategory.isActive())
            .name(productCategory.getName())
            .description(productCategory.getDescription())
            .parent(toEntity(productCategory.getParent()))
            .build();
    }

    public ProductCategory toDomain(ProductCategoryEntity productCategoryEntity) {
        if (productCategoryEntity == null) {
            return null;
        }
        return ProductCategory.builder()
            .id(productCategoryEntity.getId())
            .active(productCategoryEntity.isActive())
            .name(productCategoryEntity.getName())
            .description(productCategoryEntity.getDescription())
            .parent(toDomain(productCategoryEntity.getParent()))
            .build();
    }
}
