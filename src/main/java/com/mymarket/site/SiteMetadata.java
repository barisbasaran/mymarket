package com.mymarket.site;

import com.mymarket.productcategory.ProductCategory;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class SiteMetadata {
    List<ProductCategory> productCategories;
    boolean loggedIn;
    String memberName;
    boolean admin;
    boolean storeOwner;
    List<String> supportedLocales;
}
