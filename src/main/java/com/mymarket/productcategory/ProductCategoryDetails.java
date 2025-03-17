package com.mymarket.productcategory;

import com.mymarket.product.Product;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class ProductCategoryDetails {
    ProductCategory productCategory;
    List<Product> products;
    List<ProductCategory> subcategories;
    List<ProductCategory> breadcrumb;
}
