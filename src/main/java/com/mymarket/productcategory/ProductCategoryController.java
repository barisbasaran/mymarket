package com.mymarket.productcategory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/product-categories")
@RequiredArgsConstructor
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    @GetMapping
    public List<ProductCategory> getAllProductCategories() {
        return productCategoryService.getAllProductCategories();
    }

    @GetMapping("/{productCategoryId}/details")
    public ProductCategoryDetails getProductCategoryDetails(@PathVariable Long productCategoryId) {
        return productCategoryService.getProductCategoryDetails(productCategoryId);
    }
}
