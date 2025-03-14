package com.mymarket.productimage;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductImageMapper {

    public ProductImageEntity toEntity(ProductImage productImage) {
        if (productImage == null) {
            return null;
        }
        return ProductImageEntity.builder()
            .id(productImage.getId())
            .url(productImage.getUrl())
            .build();
    }

    public ProductImage toDomain(ProductImageEntity productImageEntity) {
        if (productImageEntity == null) {
            return null;
        }
        return ProductImage.builder()
            .id(productImageEntity.getId())
            .url(productImageEntity.getUrl())
            .build();
    }
}
