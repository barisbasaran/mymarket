package com.mymarket.productcategory;

import com.mymarket.search.IndexService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/admin/product-categories")
@AllArgsConstructor
public class ProductCategoryAdminController {

    private ProductCategoryService productCategoryService;
    private ProductCategoryHierarchyService productCategoryHierarchyService;
    private IndexService indexService;

    @GetMapping
    public List<ProductCategory> getAllProductCategories() {
        return productCategoryService.getAllProductCategories();
    }

    @GetMapping("tree")
    public List<ProductCategoryNode> getProductCategoryTree() {
        return productCategoryHierarchyService.getProductCategoryTree(null);
    }

    @GetMapping("/{productCategoryId}")
    public ProductCategory getProductCategory(@PathVariable Long productCategoryId) {
        return productCategoryService.getProductCategoryWithPotentialParents(productCategoryId);
    }

    @PostMapping
    @SneakyThrows
    public ResponseEntity<ProductCategory> createProductCategory(
        @Valid @RequestBody ProductCategory productCategory
    ) {
        var productCategoryCreated = productCategoryService.createProductCategory(productCategory);
        indexService.updateProductCategory(productCategoryCreated);

        var uri = new URI("/service/product-categories/" + productCategoryCreated.getId());
        return ResponseEntity.created(uri).body(productCategoryCreated);
    }

    @PutMapping("/{productCategoryId}")
    public ProductCategory updateProductCategory(
        @PathVariable Long productCategoryId,
        @Valid @RequestBody ProductCategory productCategory
    ) {
        productCategory.setId(productCategoryId);

        var updateProductCategory = productCategoryService.updateProductCategory(productCategory);
        indexService.updateProductCategory(updateProductCategory);

        return updateProductCategory;
    }

    @DeleteMapping("/{productCategoryId}")
    public ResponseEntity<?> deleteProductCategory(@PathVariable Long productCategoryId) {
        productCategoryService.deleteProductCategory(productCategoryId);
        indexService.deleteProductCategory(productCategoryId);
        return ResponseEntity.ok().body(Map.of());
    }
}
