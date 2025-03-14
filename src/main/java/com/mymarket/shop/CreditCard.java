package com.mymarket.shop;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreditCard {

    @NotNull(message = "{error.credit-card.name}")
    @Size(min = 1, max = 100, message = "{error.credit-card.name}")
    private String name;

    @NotNull(message = "{error.credit-card.number}")
    @Pattern(regexp = "^[0-9]{16}$", message = "{error.credit-card.number}")
    private String number;

    @NotNull(message = "{error.credit-card.expiryMonth}")
    @Size(min = 2, max = 2, message = "{error.credit-card.expiryMonth}")
    private String expiryMonth;

    @NotNull(message = "{error.credit-card.expiryYear}")
    @Size(min = 4, max = 4, message = "{error.credit-card.expiryYear}")
    private String expiryYear;

    @NotNull(message = "{error.credit-card.cvv}")
    @Size(min = 3, max = 3, message = "{error.credit-card.cvv}")
    private String cvv;
}
