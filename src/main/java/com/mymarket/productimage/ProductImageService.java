package com.mymarket.productimage;

import com.mymarket.product.Product;
import com.mymarket.product.ProductMapper;
import com.mymarket.product.ProductNotFoundException;
import com.mymarket.product.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class ProductImageService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductImageFileService productImageFileService;

    public Product updateProductImages(Long productId, MultipartFile[] files) {
        var productEntity = productRepository.findById(productId)
            .orElseThrow(ProductNotFoundException::new);

        productImageFileService.uploadImage(productId, files);

        var images = Arrays.stream(files).map(file ->
                ProductImageEntity.builder().url(
                        String.format("/uploads/%d/%s", productId, file.getOriginalFilename()))
                    .product(productEntity).build())
            .toList();

        productEntity.getImages().addAll(images);
        var updatedProductEntity = productRepository.save(productEntity);

        var updatedProduct = productMapper.toDomain(updatedProductEntity);
        log.info("product images updated {}", updatedProduct);
        return updatedProduct;
    }

    public Product deleteProductImage(Long productId, Long imageId) {
        var productEntity = productRepository.findById(productId)
            .orElseThrow(ProductNotFoundException::new);

        var image = productEntity.getImages().stream()
            .filter(it -> it.getId().equals(imageId)).findFirst()
            .orElseThrow();

        productEntity.getImages().remove(image);
        productRepository.save(productEntity);
        productImageFileService.deleteImage(productId, imageId, image.getUrl());

        log.info("Deleted image {} for product {}", image.getId(), productId);

        return productMapper.toDomain(productEntity);
    }
}
