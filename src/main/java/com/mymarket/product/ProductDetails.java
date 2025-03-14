package com.mymarket.product;

import com.mymarket.productcategory.ProductCategory;
import com.mymarket.review.Review;
import com.mymarket.review.ReviewSummary;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class ProductDetails {
    Product product;
    List<ProductCategory> breadcrumb;
    List<Product> similarProducts;
    List<Review> reviews;
    ReviewSummary reviewSummary;
    boolean myProduct;
}
