package com.mymarket.search;

import com.mymarket.product.Product;
import com.mymarket.product.ProductService;
import com.mymarket.productcategory.ProductCategory;
import com.mymarket.productcategory.ProductCategoryService;
import com.mymarket.store.Store;
import com.mymarket.store.StoreService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@AllArgsConstructor
@Service
@Slf4j
public class IndexService {

    private final ElasticsearchOperations operations;
    private final SProductRepository sProductRepository;
    private final ProductService productService;
    private final ProductCategoryService productCategoryService;
    private final StoreService storeService;

    @PostConstruct
    public void indexProducts() {
        operations.indexOps(SProduct.class).delete();
        operations.indexOps(SProduct.class).create();

        var products = productService.getAllProducts().stream()
            .map(this::mapProduct).toList();
        sProductRepository.saveAll(products);

        var productCategories = productCategoryService.getAllProductCategories().stream()
            .map(this::mapProductCategory).toList();
        sProductRepository.saveAll(productCategories);

        var stores = storeService.getAllStores().stream()
            .map(this::mapStore).toList();
        sProductRepository.saveAll(stores);
    }

    public void updateProduct(Product product) {
        sProductRepository.save(mapProduct(product));
    }

    public void updateProductCategory(ProductCategory productCategory) {
        sProductRepository.save(mapProductCategory(productCategory));
    }

    public void deleteProduct(Long productId) {
        sProductRepository.deleteById(getProductId(productId));
    }

    public void deleteProductCategory(Long productCategoryId) {
        sProductRepository.deleteById(getProductCategoryId(productCategoryId));
    }

    private SProduct mapProduct(Product product) {
        return SProduct.builder()
            .id(getProductId(product.getId()))
            .name(product.getName())
            .url("/product/view-details.html?p=" + product.getId())
            .keywords(Arrays.asList(product.getDescription().split(" ")))
            .build();
    }

    private SProduct mapProductCategory(ProductCategory productCategory) {
        return SProduct.builder()
            .id(getProductCategoryId(productCategory.getId()))
            .name(productCategory.getName())
            .url("/product-category/view-details.html?pc=" + productCategory.getId())
            .keywords(Arrays.asList(productCategory.getDescription().split(" ")))
            .build();
    }

    private SProduct mapStore(Store store) {
        return SProduct.builder()
            .id(getStoreId(store.getId()))
            .name(store.getName())
            .url("/store/view-details.html?s=" + store.getId())
            .build();
    }

    private String getProductId(Long productId) {
        return "product-" + productId;
    }

    private String getProductCategoryId(Long productCategoryId) {
        return "product-category-" + productCategoryId;
    }

    private String getStoreId(Long storeId) {
        return "store-" + storeId;
    }
}
