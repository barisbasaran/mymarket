package com.mymarket.productcategory;

import com.mymarket.web.error.ApplicationException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductCategoryHierarchyService {

    private final ProductCategoryRepository productCategoryRepository;

    public List<ProductCategoryNode> getProductCategoryTree(Long productCategoryId) {
        return productCategoryRepository.findByParent(productCategoryId == null ? null :
                ProductCategoryEntity.builder().id(productCategoryId).build())
            .stream()
            .map(category -> ProductCategoryNode.builder()
                .id(category.getId())
                .active(category.isActive())
                .name(category.getName())
                .children(getProductCategoryTree(category.getId()))
                .build())
            .toList();
    }

    public void checkParentNotCircular(ProductCategory productCategory) {
        var allChildrenAndSelfIds = getAllChildrenAndSelfIds(productCategory.getId());
        if (productCategory.getParent() != null
            && allChildrenAndSelfIds.contains(productCategory.getParent().getId())) {
            throw new ApplicationException("error.parent.circular");
        }
    }

    public List<Long> getAllChildrenAndSelfIds(Long productCategoryId) {
        var result = new ArrayList<>(getAllChildrenIds(productCategoryId));
        result.add(productCategoryId);
        return result;
    }

    public List<Long> getAllChildrenIds(Long productCategoryId) {
        var children = productCategoryRepository.findByParent(ProductCategoryEntity.builder().id(productCategoryId).build());
        var result = new ArrayList<>(children.stream().map(ProductCategoryEntity::getId).toList());
        var subChildren = children.stream()
            .map(child -> getAllChildrenIds(child.getId()))
            .flatMap(List::stream)
            .toList();
        result.addAll(subChildren);
        return result;
    }
}
