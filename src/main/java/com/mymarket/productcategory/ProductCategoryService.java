package com.mymarket.productcategory;

import com.mymarket.product.Product;
import com.mymarket.product.ProductService;
import com.mymarket.web.error.ApplicationException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Comparator.comparing;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductCategoryService {

    private final ProductCategoryMapper productCategoryMapper;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductCategoryHierarchyService productCategoryHierarchyService;
    private final ProductService productService;
    private final BreadcrumbService breadcrumbService;

    public List<ProductCategory> getAllProductCategories() {
        return productCategoryRepository.findAll().stream()
            .map(productCategoryMapper::toDomain)
            .sorted(comparing(ProductCategory::getName))
            .toList();
    }

    public List<ProductCategory> getNavBarProductCategories() {
        return productCategoryRepository.findByParent(null).stream()
            .filter(ProductCategoryEntity::isActive)
            .map(productCategoryMapper::toDomain)
            .sorted(comparing(ProductCategory::getName))
            .toList();
    }

    public ProductCategory getProductCategoryWithPotentialParents(Long productCategoryId) {
        return productCategoryRepository.findById(productCategoryId)
            .map(productCategoryMapper::toDomain)
            .map(productCategory -> {
                productCategory.setPotentialParents(getPotentialParents(productCategory.getId()));
                return productCategory;
            })
            .orElseThrow(ProductCategoryNotFoundException::new);
    }

    public ProductCategoryDetails getProductCategoryDetails(Long productCategoryId) {
        var productCategoryEntity = getProductCategoryEntity(productCategoryId);
        var subcategories = productCategoryRepository.findByParent(productCategoryEntity).stream()
            .map(productCategoryMapper::toDomain).toList();
        var productCategory = productCategoryMapper.toDomain(productCategoryEntity);
        return ProductCategoryDetails.builder()
            .productCategory(productCategory)
            .products(getProducts(productCategoryId))
            .subcategories(subcategories)
            .breadcrumb(breadcrumbService.getBreadcrumb(productCategory.getParent()))
            .build();
    }

    public ProductCategory createProductCategory(ProductCategory productCategory) {
        // clean any potential id values set
        productCategory.setId(null);

        var productCategoryEntity = productCategoryMapper.toEntity(productCategory);
        var createdProductCategoryEntity = productCategoryRepository.save(productCategoryEntity);

        var createdProductCategory = productCategoryMapper.toDomain(createdProductCategoryEntity);
        log.info("product category created {}", createdProductCategory);
        return createdProductCategory;
    }

    public ProductCategory updateProductCategory(ProductCategory productCategory) {
        var productCategoryEntity = getProductCategoryEntity(productCategory.getId());
        productCategoryHierarchyService.checkParentNotCircular(productCategory);

        productCategoryEntity.setActive(productCategory.isActive());
        productCategoryEntity.setName(productCategory.getName());
        if (productCategory.getParent() != null) {
            productCategoryEntity.setParent(
                ProductCategoryEntity.builder().id(productCategory.getParent().getId()).build());
        } else {
            productCategoryEntity.setParent(null);
        }
        productCategoryEntity.setDescription(productCategory.getDescription());
        var updatedProductCategoryEntity = productCategoryRepository.save(productCategoryEntity);

        var updatedProductCategory = productCategoryMapper.toDomain(updatedProductCategoryEntity);
        log.info("product category updated {}", updatedProductCategory);
        return updatedProductCategory;
    }

    public void deleteProductCategory(Long productCategoryId) {
        if (productService.findByProductCategory(productCategoryId).isEmpty()) {
            productCategoryRepository.deleteById(productCategoryId);
            log.info("product category deleted {}", productCategoryId);
        } else {
            throw new ApplicationException("category-has-products");
        }
    }

    private ProductCategoryEntity getProductCategoryEntity(Long productCategoryId) {
        return productCategoryRepository.findById(productCategoryId)
            .orElseThrow(ProductCategoryNotFoundException::new);
    }

    private List<Product> getProducts(Long productCategoryId) {
        return productCategoryHierarchyService.getAllChildrenAndSelfIds(productCategoryId).stream()
            .map(productService::findByProductCategory)
            .flatMap(List::stream)
            .toList();
    }

    private List<ProductCategory> getPotentialParents(Long productCategoryId) {
        var allChildrenAndSelfIds = productCategoryHierarchyService.getAllChildrenAndSelfIds(productCategoryId);
        return productCategoryRepository.findAll().stream()
            .filter(category -> !allChildrenAndSelfIds.contains(category.getId()))
            .map(productCategoryMapper::toDomain)
            .sorted(comparing(ProductCategory::getName))
            .toList();
    }
}
