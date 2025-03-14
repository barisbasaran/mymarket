package com.mymarket.product;

import com.mymarket.productcategory.BreadcrumbService;
import com.mymarket.productcategory.ProductCategoryHierarchyService;
import com.mymarket.productcategory.ProductCategoryNotFoundException;
import com.mymarket.review.Review;
import com.mymarket.review.ReviewService;
import com.mymarket.review.ReviewSummary;
import com.mymarket.store.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final BreadcrumbService breadcrumbService;
    private final ProductCategoryHierarchyService productCategoryHierarchyService;
    private final ReviewService reviewService;
    private final StoreRepository storeRepository;

    public List<Product> getMyProducts(Long memberId) {
        return storeRepository.findByMemberId(memberId).stream()
            .map(storeEntity -> getStoreProducts(storeEntity.getId()))
            .flatMap(List::stream)
            .toList();
    }

    public List<Product> getStoreProducts(Long storeId) {
        return productRepository.findByStore_Id(storeId).stream()
            .map(productMapper::toDomain)
            .toList();
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll().stream()
            .map(productMapper::toDomain)
            .toList();
    }

    public List<Product> getProducts(List<Long> productIds) {
        return productIds.stream().map(this::getProduct).toList();
    }

    public Product getProduct(Long productId) {
        return productRepository.findById(productId)
            .map(productMapper::toDomain)
            .orElseThrow(ProductNotFoundException::new);
    }

    @SneakyThrows
    public ProductDetails getProductDetails(Long productId, Long memberId) {
        var reviews = reviewService.getProductReviews(productId);
        var reviewStatistics = reviews.stream()
            .map(Review::getRating)
            .mapToInt(Integer::intValue)
            .summaryStatistics();
        var product = productRepository.findById(productId).map(productMapper::toDomain)
            .orElseThrow(ProductNotFoundException::new);
        return ProductDetails.builder()
            .product(product)
            .breadcrumb(breadcrumbService.getBreadcrumb(product.getProductCategory()))
            .similarProducts(getSimilarProducts(product))
            .reviews(reviews)
            .reviewSummary(ReviewSummary.builder()
                .rating((int) reviewStatistics.getAverage())
                .count(reviewStatistics.getCount())
                .build())
            .myProduct(product.getStore().getMember().getId().equals(memberId))
            .build();
    }

    public List<Product> findByProductCategory(Long productCategoryId) {
        var products = productRepository.findByProductCategoryId(productCategoryId).stream()
            .map(productMapper::toDomain)
            .toList();
        log.info("products found for category {} : {}", productCategoryId, products);
        return products;
    }

    public Product createProduct(Product product) {
        validateProduct(product);
        // clean any potential id values set
        product.setId(null);
        product.getImages().forEach(image -> image.setId(null));

        var productEntity = productMapper.toEntity(product);
        productEntity.getImages().forEach(image -> image.setProduct(productEntity));
        var createdProductEntity = productRepository.save(productEntity);

        var createdProduct = productMapper.toDomain(createdProductEntity);
        log.info("product created {}", createdProduct);
        return createdProduct;
    }

    public Product updateProduct(Product product) {
        validateProduct(product);
        var productEntity = productRepository.findById(product.getId())
            .orElseThrow(ProductNotFoundException::new);

        var inputProductEntity = productMapper.toEntity(product);

        productEntity.setName(inputProductEntity.getName());
        productEntity.setDescription(inputProductEntity.getDescription());
        productEntity.setSpecs(inputProductEntity.getSpecs());
        productEntity.setProductCategory(inputProductEntity.getProductCategory());
        productEntity.setStore(inputProductEntity.getStore());
        productEntity.setActive(inputProductEntity.isActive());
        productEntity.setPriceAmount(inputProductEntity.getPriceAmount());
        productEntity.setPriceCurrency(inputProductEntity.getPriceCurrency());
        var updatedProductEntity = productRepository.save(productEntity);

        var updatedProduct = productMapper.toDomain(updatedProductEntity);
        log.info("product updated {}", updatedProduct);
        return updatedProduct;
    }

    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
        log.info("product deleted {}", productId);
    }

    private void validateProduct(Product product) {
        var productCategory = product.getProductCategory();
        if (productCategory == null || productCategory.getId() == null) {
            throw new ProductCategoryNotFoundException();
        }
    }

    private List<Product> getSimilarProducts(Product product) {
        return productCategoryHierarchyService.getAllChildrenAndSelfIds(product.getProductCategory().getId())
            .stream()
            .map(this::findByProductCategory)
            .flatMap(List::stream)
            .filter(it -> !it.getId().equals(product.getId()))
            .filter(it -> !it.getImages().isEmpty())
            .limit(8)
            .toList();
    }
}
