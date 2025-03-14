package com.mymarket.product;

import com.mymarket.productcategory.ProductCategory;
import com.mymarket.productimage.ProductImage;
import com.mymarket.store.Store;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Product {

    private Long id;

    private boolean active;

    @NotNull(message = "{error.product-category}")
    private ProductCategory productCategory;

    @Size(min = 1, max = 300, message = "{error.product.name}")
    private String name;

    @Size(max = 5000, message = "{error.product.description}")
    private String description;

    @NotNull
    private List<ProductImage> images;

    @Valid
    private Price price;

    private String specs;

    @NotNull(message = "{error.store}")
    private Store store;
}
