package com.mymarket.site;

import com.mymarket.productcategory.ProductCategory;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SiteMetadata {

    private List<ProductCategory> productCategories;

    private boolean loggedIn;

    private String memberName;

    private boolean admin;

    private boolean storeOwner;
}
