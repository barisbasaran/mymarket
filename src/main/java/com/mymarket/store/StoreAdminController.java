package com.mymarket.store;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/stores")
@RequiredArgsConstructor
public class StoreAdminController {

    private final StoreService storeService;

    @GetMapping
    public List<Store> getAllStores() {
        return storeService.getAllStores();
    }
}
