package com.mymarket.productimage;

import com.mymarket.product.Product;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/products/{productId}/images")
@AllArgsConstructor
public class ProductImageController {

    private ProductImageService productImageService;

    @PostMapping
    @PreAuthorize("hasRole('STORE_OWNER')")
    public Product uploadImage(
        @PathVariable Long productId,
        @RequestParam("fileName") MultipartFile[] files
    ) {
        return productImageService.updateProductImages(productId, files);
    }

    @DeleteMapping("{imageId}")
    @PreAuthorize("hasRole('STORE_OWNER')")
    public Product deleteImage(
        @PathVariable Long productId,
        @PathVariable Long imageId
    ) {
        return productImageService.deleteProductImage(productId, imageId);
    }
}
