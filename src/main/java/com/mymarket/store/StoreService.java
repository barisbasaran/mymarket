package com.mymarket.store;

import com.mymarket.product.ProductService;
import com.mymarket.web.error.ApplicationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;
    private final ProductService productService;

    public Store getStore(Long storeId) {
        return storeRepository.findById(storeId)
            .map(storeMapper::toDomain)
            .orElseThrow(StoreNotFoundException::new);
    }

    public List<Store> getAllStores() {
        return storeRepository.findAll().stream()
            .map(storeMapper::toDomain)
            .toList();
    }

    public Store createStore(Store store) {
        var storeEntity = storeMapper.toEntity(store);
        try {
            var storeCreated = storeMapper.toDomain(storeRepository.save(storeEntity));
            log.info("Store created: {}", storeCreated);
            return storeCreated;
        } catch (DataIntegrityViolationException ex) {
            throw new ApplicationException("store-name-exists");
        }
    }

    public Store updateStore(Long storeId, Store store) {
        var storeEntity = storeRepository.findById(storeId)
            .orElseThrow(StoreNotFoundException::new);

        storeEntity.setName(store.getName());
        try {
            var storeUpdated = storeMapper.toDomain(storeRepository.save(storeEntity));
            log.info("Store updated: {}", storeUpdated);
            return storeUpdated;
        } catch (DataIntegrityViolationException ex) {
            throw new ApplicationException("store-name-exists");
        }
    }

    public List<Store> getMyStores(Long memberId) {
        return storeRepository.findByMemberId(memberId).stream()
            .map(storeMapper::toDomain)
            .sorted(Comparator.comparing(Store::getName))
            .toList();
    }

    public StoreDetails getStoreDetails(Long storeId) {
        var store = getStore(storeId);
        return StoreDetails.builder()
            .id(store.getId())
            .name(store.getName())
            .products(productService.getStoreProducts(storeId))
            .build();
    }
}
