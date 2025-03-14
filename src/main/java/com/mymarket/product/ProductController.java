package com.mymarket.product;

import com.mymarket.membership.member.Member;
import com.mymarket.membership.member.MemberNotLoggedInException;
import com.mymarket.membership.member.MemberService;
import com.mymarket.search.IndexService;
import com.mymarket.store.StoreValidator;
import com.mymarket.web.error.ApplicationException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {

    private final StoreValidator storeValidator;
    private final ProductService productService;
    private final ProductValidator productValidator;
    private final IndexService indexService;
    private final MemberService memberService;

    @GetMapping("{productId}/details")
    public ProductDetails getProductDetails(@PathVariable Long productId) {
        var memberId = memberService.getCurrentMember().map(Member::getId).orElse(null);
        return productService.getProductDetails(productId, memberId);
    }

    @GetMapping("grouped")
    public List<Product> getProducts(@RequestParam("p") List<Long> productIds) {
        return productService.getProducts(productIds);
    }

    @GetMapping("{productId}")
    @PreAuthorize("hasRole('STORE_OWNER')")
    public Product getProduct(@PathVariable Long productId) {
        var currentMember = memberService.getCurrentMember()
            .orElseThrow(MemberNotLoggedInException::new);
        storeValidator.validateMyProduct(currentMember.getId(), productId);
        return productService.getProduct(productId);
    }

    @GetMapping
    @PreAuthorize("hasRole('STORE_OWNER')")
    public List<Product> getMyProducts() {
        var currentMember = memberService.getCurrentMember()
            .orElseThrow(MemberNotLoggedInException::new);
        return productService.getMyProducts(currentMember.getId());
    }

    @PostMapping
    @SneakyThrows
    @PreAuthorize("hasRole('STORE_OWNER')")
    public ResponseEntity<Product> createProduct(@RequestBody Product product, BindingResult bindingResult) {
        productValidator.validate(product, bindingResult);

        var productCreated = productService.createProduct(product);
        indexService.updateProduct(productCreated);

        var uri = new URI("/service/products/" + productCreated.getId());
        return ResponseEntity.created(uri).body(productCreated);
    }

    @PutMapping("{productId}")
    @PreAuthorize("hasRole('STORE_OWNER')")
    public Product updateProduct(
        @PathVariable Long productId,
        @RequestBody Product product,
        BindingResult bindingResult
    ) {
        var currentMember = memberService.getCurrentMember()
            .orElseThrow(MemberNotLoggedInException::new);
        storeValidator.validateMyProduct(currentMember.getId(), productId);
        productValidator.validate(product, bindingResult);

        product.setId(productId);
        var updatedProduct = productService.updateProduct(product);
        indexService.updateProduct(updatedProduct);

        return updatedProduct;
    }

    @DeleteMapping("{productId}")
    @PreAuthorize("hasRole('STORE_OWNER')")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        try {
            productService.deleteProduct(productId);
        } catch (DataIntegrityViolationException ex) {
            log.error("Error deleting product {}", ex.getMessage());
            throw new ApplicationException("product-sold-cannot-be-deleted");
        }
        indexService.deleteProduct(productId);
        return ResponseEntity.ok().body(Map.of());
    }
}
