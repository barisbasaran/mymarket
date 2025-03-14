package com.mymarket.productcategory;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class ProductCategoryNode {

    Long id;

    boolean active;

    String name;

    List<ProductCategoryNode> children;
}
