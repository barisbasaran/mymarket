package com.mymarket.product;

import com.mymarket.web.error.ApplicationException;

public class ProductNotFoundException extends ApplicationException {

    public ProductNotFoundException() {
        super("product-not-found");
    }
}
