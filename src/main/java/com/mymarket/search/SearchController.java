package com.mymarket.search;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/search")
@AllArgsConstructor
public class SearchController {

    private SearchService searchService;

    @GetMapping
    public SearchResult search(
        @RequestParam
        @NotBlank(message = "enter-search-term")
        String query,
        Pageable pageable
    ) {
        return searchService.search(query, pageable);
    }
}
