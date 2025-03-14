package com.mymarket.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Digits;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Price {

    @JsonFormat(pattern = ".##")
    @Digits(integer = 10, fraction = 2, message = "{error.price.amount}")
    private BigDecimal amount;

    private Currency currency;
}
