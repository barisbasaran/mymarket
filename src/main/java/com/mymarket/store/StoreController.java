package com.mymarket.store;

import com.mymarket.membership.member.MemberNotLoggedInException;
import com.mymarket.membership.member.MemberService;
import com.mymarket.search.IndexService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;
    private final StoreValidator storeValidator;
    private final MemberService memberService;
    private final IndexService indexService;

    @GetMapping("{storeId}/details")
    public StoreDetails getStoreDetails(@PathVariable Long storeId) {
        return storeService.getStoreDetails(storeId);
    }

    @GetMapping
    @PreAuthorize("hasRole('STORE_OWNER')")
    public List<Store> getMyStores() {
        var currentMember = memberService.getCurrentMember()
            .orElseThrow(MemberNotLoggedInException::new);
        return storeService.getMyStores(currentMember.getId());
    }

    @GetMapping("{storeId}")
    @PreAuthorize("hasRole('STORE_OWNER')")
    public Store getStore(@PathVariable Long storeId) {
        var currentMember = memberService.getCurrentMember()
            .orElseThrow(MemberNotLoggedInException::new);
        storeValidator.validateMyStore(currentMember.getId(), storeId);

        return storeService.getStore(storeId);
    }

    @PostMapping
    @SneakyThrows
    @PreAuthorize("hasRole('STORE_OWNER')")
    public ResponseEntity<Store> createStore(@RequestBody @Valid Store store) {
        var currentMember = memberService.getCurrentMember()
            .orElseThrow(MemberNotLoggedInException::new);
        store.setMember(currentMember);

        var storeCreated = storeService.createStore(store);
        indexService.updateStore(storeCreated);

        var uri = new URI("/service/products/" + storeCreated.getId());
        return ResponseEntity.created(uri).body(storeCreated);
    }

    @PutMapping("{storeId}")
    @PreAuthorize("hasRole('STORE_OWNER')")
    public Store updateStore(@PathVariable Long storeId, @RequestBody @Valid Store store) {
        var currentMember = memberService.getCurrentMember()
            .orElseThrow(MemberNotLoggedInException::new);
        storeValidator.validateMyStore(currentMember.getId(), storeId);
        store.setMember(currentMember);
        store.setId(storeId);

        var updatedStore = storeService.updateStore(storeId, store);
        indexService.updateStore(updatedStore);

        return updatedStore;
    }
}
