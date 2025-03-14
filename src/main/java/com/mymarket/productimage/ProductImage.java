package com.mymarket.productimage;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductImage {

    @NotNull
    private Long id;

    @NotBlank
    private String url;
}
