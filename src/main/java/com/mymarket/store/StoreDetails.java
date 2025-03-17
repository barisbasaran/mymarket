package com.mymarket.store;

import com.mymarket.product.Product;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class StoreDetails {
     Long id;
     String name;
    List<Product> products;
}
