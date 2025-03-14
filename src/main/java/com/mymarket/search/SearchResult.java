package com.mymarket.search;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class SearchResult {
    List<SProduct> items;
    long totalHits;
}
