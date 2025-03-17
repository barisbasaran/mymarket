package com.mymarket.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SProductRepository extends ElasticsearchRepository<SProduct, String> {
}
