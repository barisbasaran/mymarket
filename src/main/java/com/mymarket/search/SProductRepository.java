package com.mymarket.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

interface SProductRepository extends ElasticsearchRepository<SProduct, String> {

}
