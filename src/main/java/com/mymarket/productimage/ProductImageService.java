package com.mymarket.productimage;

import com.mymarket.product.Product;
import com.mymarket.product.ProductMapper;
import com.mymarket.product.ProductNotFoundException;
import com.mymarket.product.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductImageService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductImageFileService productImageFileService;
    private final ProductImageRepository productImageRepository;

    public Product updateProductImages(Long productId, MultipartFile[] files) {
        var productEntity = productRepository.findById(productId)
            .orElseThrow(ProductNotFoundException::new);
        boolean hasNoImage = productEntity.getImages().isEmpty();

        productImageFileService.uploadImage(productId, files);

        var images = IntStream.range(0, files.length).mapToObj(i ->
            ProductImageEntity.builder().url(
                    String.format("/uploads/%d/%s", productId, files[i].getOriginalFilename()))
                .product(productEntity)
                .coverImage(hasNoImage && i == 0)
                .build()
        ).toList();
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

    public Product setCoverImage(Long productId, Long imageId) {
        productImageRepository.findByProductId(productId).forEach(productImageEntity -> {
            productImageEntity.setCoverImage(productImageEntity.getId().equals(imageId));
            productImageRepository.save(productImageEntity);
        });
        var productEntity = productRepository.findById(productId)
            .orElseThrow(ProductNotFoundException::new);
        return productMapper.toDomain(productEntity);
    }
}
