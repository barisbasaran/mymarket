package com.mymarket.productcategory;

import com.mymarket.web.error.ApplicationException;

public class ProductCategoryNotFoundException extends ApplicationException {

    public ProductCategoryNotFoundException() {
        super("product-category-not-found");
    }
}
