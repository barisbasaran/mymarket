package com.mymarket.productcategory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BreadcrumbService {

    public List<ProductCategory> getBreadcrumb(ProductCategory productCategory) {
        return getBreadcrumbInternal(productCategory).stream()
            .toList()
            .reversed();
    }

    private List<ProductCategory> getBreadcrumbInternal(ProductCategory productCategory) {
        if (productCategory == null) {
            return List.of();
        } else {
            var breadcrumb = new ArrayList<ProductCategory>();
            breadcrumb.add(productCategory);
            breadcrumb.addAll(getBreadcrumbInternal(productCategory.getParent()));
            return breadcrumb;
        }
    }
}
