package com.mymarket.shop;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Checkout {

    @NotNull(message = "{error.shipment.provided}")
    @Valid
    private Shipment shipment;

    @NotNull(message = "{error.credit-card.provided}")
    @Valid
    private CreditCard card;

    @NotEmpty(message = "{error.checkout.items}")
    private List<CartItem> items;
}
