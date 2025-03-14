package com.mymarket.review;

import com.mymarket.shop.OrderItem;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Review {

    private Long id;

    @Min(value = 1, message = "{error.review.rating}")
    @Max(value = 5, message = "{error.review.rating}")
    private int rating;

    @Size(max = 3000, message = "{error.review.comment}")
    private String comment;

    @NotNull
    @Valid
    private OrderItem orderItem;
}
