package com.mymarket.store;

import com.mymarket.product.ProductService;
import com.mymarket.web.error.ApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class StoreValidator {

    private final StoreRepository storeRepository;
    private final ProductService productService;

    public void validateMyProduct(Long memberId, Long productId) {
        var storeId = productService.getProduct(productId).getStore().getId();
        validateMyStore(memberId, storeId);
    }

    public void validateMyStore(Long memberId, Long storeId) {
        if (isOthersStore(memberId, storeId)) {
            log.error("Store {} does not belong to member {}", storeId, memberId);
            throw new ApplicationException("store-violation");
        }
    }

    public boolean isOthersStore(Long memberId, Long storeId) {
        return storeRepository.findByIdAndMemberId(storeId, memberId).isEmpty();
    }
}
