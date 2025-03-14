package com.mymarket.site;

import com.mymarket.membership.member.MemberService;
import com.mymarket.productcategory.ProductCategoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/sites")
@AllArgsConstructor
public class SiteController {

    private ProductCategoryService productCategoryService;
    private final MemberService memberService;

    @GetMapping("/metadata")
    public SiteMetadata getActiveProductCategories(HttpServletRequest request) {
        var siteMetadataBuilder = SiteMetadata.builder()
            .productCategories(productCategoryService.getNavBarProductCategories());

        var currentMember = memberService.getCurrentMember();
        if (currentMember.isPresent()) {
            return siteMetadataBuilder.loggedIn(true)
                .memberName(currentMember.get().getFirstName())
                .admin(currentMember.get().isAdmin())
                .storeOwner(currentMember.get().isStoreOwner())
                .build();
        } else {
            return siteMetadataBuilder.loggedIn(false).build();
        }
    }


    @GetMapping("/csrf")
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }
}
