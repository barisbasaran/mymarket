package com.mymarket.shop;

import com.mymarket.product.Price;
import com.mymarket.product.Product;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItem {

    private Long id;

    private Product product;

    private Long quantity;

    private Price price;

    private int reviewRating;
}
