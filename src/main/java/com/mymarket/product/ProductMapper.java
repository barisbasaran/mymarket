package com.mymarket.product;

import com.mymarket.productimage.ProductImageMapper;
import com.mymarket.productcategory.ProductCategoryMapper;
import com.mymarket.store.StoreMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductMapper {

    private final ProductImageMapper productImageMapper;
    private final ProductCategoryMapper productCategoryMapper;
    private final StoreMapper storeMapper;

    public ProductEntity toEntity(Product product) {
        if (product == null) {
            return null;
        }
        return ProductEntity.builder()
            .id(product.getId())
            .productCategory(productCategoryMapper.toEntity(product.getProductCategory()))
            .store(storeMapper.toEntity(product.getStore()))
            .active(product.isActive())
            .name(product.getName())
            .description(product.getDescription())
            .specs(product.getSpecs())
            .priceAmount(product.getPrice() != null ? product.getPrice().getAmount() : null)
            .priceCurrency(product.getPrice() != null ? product.getPrice().getCurrency() : null)
            .images(product.getImages().stream().map(productImageMapper::toEntity).toList())
            .build();
    }

    public Product toDomain(ProductEntity productEntity) {
        if (productEntity == null) {
            return null;
        }
        return Product.builder()
            .id(productEntity.getId())
            .productCategory(productCategoryMapper.toDomain(productEntity.getProductCategory()))
            .store(storeMapper.toDomain(productEntity.getStore()))
            .active(productEntity.isActive())
            .name(productEntity.getName())
            .description(productEntity.getDescription())
            .specs(productEntity.getSpecs())
            .price(productEntity.getPriceAmount() != null ? Price.builder()
                .amount(productEntity.getPriceAmount())
                .currency(productEntity.getPriceCurrency()).build() : null)
            .images(productEntity.getImages() != null ? productEntity.getImages().stream().map(productImageMapper::toDomain).toList() : List.of())
            .build();
    }
}
