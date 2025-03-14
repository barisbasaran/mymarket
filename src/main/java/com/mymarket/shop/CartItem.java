package com.mymarket.shop;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItem {

    @NotNull(message = "{error.checkout.product}")
    private Long productId;

    @NotNull(message = "{error.checkout.quantity}")
    private Long quantity;
}
